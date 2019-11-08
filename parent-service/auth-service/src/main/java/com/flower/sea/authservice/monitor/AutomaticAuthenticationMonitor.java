package com.flower.sea.authservice.monitor;

import com.flower.sea.commonservice.annotation.AuthorityAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自动上传权限API监听类
 *
 * @author zhangLei
 * @serial 2019/11/8
 */
@Component
@Slf4j
public class AutomaticAuthenticationMonitor implements CommandLineRunner {

    private final WebApplicationContext applicationContext;


    @Autowired
    public AutomaticAuthenticationMonitor(WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(String... args) {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        List<Map<String, String>> recordList = new ArrayList<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            HandlerMethod method = m.getValue();
            if (null != AnnotationUtils.findAnnotation(method.getMethod(), AuthorityAnnotation.class)) {
                RequestMappingInfo info = m.getKey();
                Map<String, String> recordMap = new HashMap<>(8);
                PatternsRequestCondition p = info.getPatternsCondition();
                for (String url : p.getPatterns()) {
                    recordMap.put("url", url);
                }
                recordMap.put("className", method.getMethod().getDeclaringClass().getName());
                recordMap.put("method", method.getMethod().getName());
                RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
                for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                    recordMap.put("type", requestMethod.toString());
                }
                recordList.add(recordMap);
            }
        }
        recordList.forEach(System.out::println);
    }
}
