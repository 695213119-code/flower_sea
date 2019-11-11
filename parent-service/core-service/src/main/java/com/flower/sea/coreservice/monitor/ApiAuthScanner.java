package com.flower.sea.coreservice.monitor;

import com.flower.sea.commonservice.annotation.ApiMenuAnnotation;
import com.flower.sea.commonservice.annotation.AuthorityAnnotation;
import com.flower.sea.commonservice.core.AuthorityApi;
import com.flower.sea.commonservice.core.AuthorityApp;
import com.flower.sea.commonservice.core.AuthorityMenu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangLei
 * @serial 2019/11/11
 */
@Component
@Slf4j
public class ApiAuthScanner implements CommandLineRunner {

    private final ApplicationContext applicationContext;
    private final Map<String, AuthorityMenu> cache = new HashMap<>(8);

    @Autowired
    public ApiAuthScanner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(String... args) throws Exception {

        AuthorityApp authorityApp = new AuthorityApp();
        authorityApp.setAppName("core-service");

        //获取所有的RequestMapping
        Map<String, HandlerMapping> allRequestMappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, HandlerMapping.class, true, false);
        Iterator<HandlerMapping> handlerMappingIterator = allRequestMappings.values().iterator();

        //获取所有的handlerMappingIterator
        while (handlerMappingIterator.hasNext()) {
            HandlerMapping handlerMapping = handlerMappingIterator.next();
            //找到RequestMappingHandlerMapping
            if (handlerMapping instanceof RequestMappingHandlerMapping) {
                //强转
                RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) handlerMapping;
                //获取含有RequestMappingHandlerMapping注解的所有方法
                Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
                Iterator<Map.Entry<RequestMappingInfo, HandlerMethod>> entryIterator = handlerMethods.entrySet().iterator();

                //循环每一个含有RequestMappingHandlerMapping(POST,GET,PUT,DELETE)注解的所有方法
                while (entryIterator.hasNext()) {
                    Map.Entry<RequestMappingInfo, HandlerMethod> methodEntry = entryIterator.next();
                    RequestMappingInfo methodEntryKey = methodEntry.getKey();
                    HandlerMethod methodEntryValue = methodEntry.getValue();
                    AuthorityApi apiAuth = findApiAuth(methodEntryValue.getMethod());
                    if (null != apiAuth) {
                        //获取该方法的所属类(controller)
                        Class<?> beanType = methodEntryValue.getBeanType();
                        //判断这个controller上是否含有ApiMenuAnnotation注解
                        ApiMenuAnnotation apiMenuAnnotation = beanType.getAnnotation(ApiMenuAnnotation.class);
                        if (null == apiMenuAnnotation) {
                            log.error("<{}>下的接口使用了@AuthorityAnnotation, 必须在Controller上添加@ApiMenu注解", beanType.getSimpleName());
                            System.exit(0);
                        }

                        //className
                        String simpleName = beanType.toString();
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


            }
        }
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
            AuthorityMenu authMenu = new AuthorityMenu();
            authMenu.setClassName(className);
            authMenu.setMenuName(apiMenuAnnotation.name());
            return cache.put(className, authMenu);
        }
        return authorityMenu;
    }
}
