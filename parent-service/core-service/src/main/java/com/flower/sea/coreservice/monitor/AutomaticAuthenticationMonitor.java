package com.flower.sea.coreservice.monitor;

import cn.hutool.core.collection.CollUtil;
import com.flower.sea.commonservice.annotation.ApiMenuAnnotation;
import com.flower.sea.commonservice.annotation.AuthorityAnnotation;
import com.flower.sea.commonservice.core.AuthorityApi;
import com.flower.sea.commonservice.core.AuthorityApp;
import com.flower.sea.commonservice.core.AuthorityMenu;
import com.flower.sea.commonservice.enumeration.middlewareEnumeration;
import com.flower.sea.commonservice.utils.ClassUtils;
import com.flower.sea.commonservice.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 自动上传权限API监听类
 *
 * @author zhangLei
 * @serial 2019/11/8
 */
@Component
@Slf4j
public class AutomaticAuthenticationMonitor implements CommandLineRunner {

    private final AmqpTemplate rabbitmqTemplate;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${app.explain}")
    private String appExplain;


    @Autowired
    public AutomaticAuthenticationMonitor(AmqpTemplate rabbitmqTemplate) {
        this.rabbitmqTemplate = rabbitmqTemplate;
    }

    @Override
    public void run(String... args) {
        log.info("----开始上传权限api----");
        AuthorityApp authorityApp = new AuthorityApp();
        authorityApp.setAppName(appName);
        authorityApp.setAppExplain(appExplain);
        List<AuthorityMenu> authorityMenuList = new ArrayList<>();
        Set<Class<?>> classSet = ClassUtils.getClassSet("com.flower.sea.coreservice.controller");
        if (CollUtil.isNotEmpty(classSet)) {
            for (Class set : classSet) {
                if (null != AnnotationUtils.findAnnotation(set, ApiMenuAnnotation.class)) {
                    ApiMenuAnnotation setAnnotation = (ApiMenuAnnotation) set.getAnnotation(ApiMenuAnnotation.class);
                    RequestMapping requestMappingAnnotation = (RequestMapping) set.getAnnotation(RequestMapping.class);
                    AuthorityMenu authorityMenu = new AuthorityMenu();
                    authorityMenu.setClassName(set.getTypeName());
                    authorityMenu.setMenuName(setAnnotation.name());
                    Method[] methods = set.getMethods();
                    List<AuthorityApi> authorityApiList = new ArrayList<>();
                    for (Method method : methods) {
                        if (null != AnnotationUtils.findAnnotation(method, AuthorityAnnotation.class)) {
                            AuthorityApi api = getMethodUrl(method, requestMappingAnnotation.value()[0]);
                            authorityApiList.add(api);
                        }
                    }
                    authorityMenu.setAuthorityApi(authorityApiList);
                    authorityMenuList.add(authorityMenu);
                }
            }
            authorityApp.setAuthorityMenu(authorityMenuList);
        }
        rabbitmqTemplate.convertAndSend(middlewareEnumeration.AUTHENTICATION_QUEUE_TYPE.getType(), JsonUtils.object2Json(authorityApp));
        log.info("----已将权限api数据上传至鉴权中心----");
    }

    /**
     * 获取方法的请求路径
     *
     * @param method method
     * @param url    当前controller的路径
     * @return AuthorityApi
     */
    private AuthorityApi getMethodUrl(Method method, String url) {
        AuthorityApi authorityApi = new AuthorityApi();
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if (null != postMapping) {
            authorityApi.setMethod("POST");
            authorityApi.setUrl(sb.append(postMapping.value()[0]).toString());
        }
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        if (null != getMapping) {
            authorityApi.setMethod("GET");
            authorityApi.setUrl(sb.append(getMapping.value()[0]).toString());
        }
        PutMapping putMapping = method.getAnnotation(PutMapping.class);
        if (null != putMapping) {
            authorityApi.setMethod("PUT");
            authorityApi.setUrl(sb.append(putMapping.value()[0]).toString());
        }
        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
        if (null != deleteMapping) {
            authorityApi.setMethod("DELETE");
            authorityApi.setUrl(sb.append(deleteMapping.value()[0]).toString());
        }
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (null != requestMapping) {
            authorityApi.setMethod("POST,GET,PUT,DELETE");
            authorityApi.setUrl(sb.append(requestMapping.value()[0]).toString());
        }
        return authorityApi;
    }


}
