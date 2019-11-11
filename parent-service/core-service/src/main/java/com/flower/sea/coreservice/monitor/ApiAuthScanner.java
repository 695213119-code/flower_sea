package com.flower.sea.coreservice.monitor;

import cn.hutool.core.collection.CollUtil;
import com.flower.sea.commonservice.annotation.ApiMenuAnnotation;
import com.flower.sea.commonservice.annotation.AuthorityAnnotation;
import com.flower.sea.commonservice.core.AuthorityApi;
import com.flower.sea.commonservice.core.AuthorityApp;
import com.flower.sea.commonservice.core.AuthorityMenu;
import com.flower.sea.commonservice.enumeration.middlewareEnumeration;
import com.flower.sea.commonservice.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;

/**
 * api权限接口上传
 *
 * @author zhangLei
 * @serial 2019/11/11
 */
@Component
@Slf4j
public class ApiAuthScanner implements CommandLineRunner {

    private final ApplicationContext applicationContext;
    private final AmqpTemplate rabbitmqTemplate;
    private Map<String, AuthorityMenu> cache = new HashMap<>(16);

    @Value("${spring.application.name}")
    private String appName;

    @Value("${app.explain}")
    private String appExplain;


    @Autowired
    public ApiAuthScanner(ApplicationContext applicationContext, AmqpTemplate rabbitmqTemplate) {
        this.applicationContext = applicationContext;
        this.rabbitmqTemplate = rabbitmqTemplate;
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("----------开始上传权限API接口----------");
        AuthorityApp authorityApp = new AuthorityApp();
        authorityApp.setAppName(appName);
        authorityApp.setAppExplain(appExplain);

        //获取所有的RequestMapping
        Map<String, HandlerMapping> allRequestMappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, HandlerMapping.class, true, false);

        //获取所有的handlerMappingIterator
        for (HandlerMapping handlerMapping : allRequestMappings.values()) {
            //找到RequestMappingHandlerMapping
            if (handlerMapping instanceof RequestMappingHandlerMapping) {
                //强转
                RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) handlerMapping;
                //获取含有RequestMappingHandlerMapping注解的所有方法
                Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();

                //循环每一个含有RequestMappingHandlerMapping(POST,GET,PUT,DELETE)注解的所有方法
                for (Map.Entry<RequestMappingInfo, HandlerMethod> methodEntry : handlerMethods.entrySet()) {
                    RequestMappingInfo methodEntryKey = methodEntry.getKey();
                    HandlerMethod methodEntryValue = methodEntry.getValue();
                    AuthorityApi apiAuth = findApiAuth(methodEntryValue.getMethod());
                    if (null != apiAuth) {
                        //获取该方法的所属类(controller)
                        Class<?> beanType = methodEntryValue.getBeanType();
                        //判断这个controller上是否含有ApiMenuAnnotation注解
                        ApiMenuAnnotation apiMenuAnnotation = beanType.getAnnotation(ApiMenuAnnotation.class);
                        if (null == apiMenuAnnotation) {
                            log.error("<{}>下的接口使用了@AuthorityAnnotation, 必须在Controller上添加@ApiMenuAnnotation注解", beanType.getSimpleName());
                            System.exit(0);
                        }

                        //simpleName
                        String simpleName = beanType.getSimpleName();
                        AuthorityMenu authorityMenu = createApiGroup(simpleName, apiMenuAnnotation);

                        //方法名称
                        Set<RequestMethod> requestMethods = methodEntryKey.getMethodsCondition().getMethods();
                        String method = requestMethods.size() == 1 ? requestMethods.iterator().next().name() : null;
                        apiAuth.setMethod(method);

                        //接口路径
                        PatternsRequestCondition patternsCondition = methodEntryKey.getPatternsCondition();
                        Set<String> patterns = patternsCondition.getPatterns();
                        patterns.forEach(apiAuth::setUrl);

                        authorityMenu.addApi(apiAuth);
                    }
                }
                if (CollUtil.isNotEmpty(cache)) {
                    List<AuthorityMenu> authorityMenuList = new ArrayList<>();
                    for (Map.Entry<String, AuthorityMenu> entry : cache.entrySet()) {
                        authorityMenuList.add(entry.getValue());
                    }
                    authorityApp.setAuthorityMenu(authorityMenuList);
                }
            }
        }
        rabbitmqTemplate.convertAndSend(middlewareEnumeration.AUTHENTICATION_QUEUE_TYPE.getType(), JsonUtils.object2Json(authorityApp));
        log.info("----------已将权限API数据上传至鉴权中心----------");
    }


    private AuthorityApi findApiAuth(Method method) {
        AuthorityAnnotation authorityAnnotation = AnnotationUtils.findAnnotation(method, AuthorityAnnotation.class);
        if (null != authorityAnnotation) {
            return new AuthorityApi();
        }
        return null;
    }


    private AuthorityMenu createApiGroup(String className, ApiMenuAnnotation apiMenuAnnotation) {
        AuthorityMenu authorityMenu = cache.get(className);
        if (null == authorityMenu) {
            authorityMenu = new AuthorityMenu();
            authorityMenu.setClassName(className);
            authorityMenu.setMenuName(apiMenuAnnotation.name());
            cache.put(className, authorityMenu);
        }
        return authorityMenu;
    }
}
