package com.flower.sea.gatewayservice.config;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于分布式的swagger配置类
 *
 * @author zhangLei
 * @serial 2019/11/5
 */
@Component
@Primary
public class DistributedSwaggerConfig implements SwaggerResourcesProvider {

    private static final String VERSION = "1.0.0";


    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        resources.add(swaggerResource("鉴权服务Swagger", "/apigateway/auth-service/v2/api-docs"));
        resources.add(swaggerResource("核心服务Swagger", "/apigateway/core-service/v2/api-docs"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(VERSION);
        return swaggerResource;
    }
}
