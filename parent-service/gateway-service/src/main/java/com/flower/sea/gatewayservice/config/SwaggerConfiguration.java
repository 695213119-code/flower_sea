package com.flower.sea.gatewayservice.config;

import cn.hutool.json.JSONArray;
import com.flower.sea.commonservice.utils.JsonUtils;
import com.flower.sea.gatewayservice.utils.GsonUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.SessionAttribute;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import javax.servlet.http.HttpSession;
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
public class SwaggerConfiguration implements SwaggerResourcesProvider {

    private static final String VERSION = "1.0.0";

    private final HttpSession session;

    @Autowired
    public SwaggerConfiguration(HttpSession session) {
        this.session = session;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        resources.add(swaggerResource("核心服务Swagger", "/apigateway/core-service/v2/api-docs"));
        resources.add(swaggerResource("用户服务Swagger", "/apigateway/user-service/v2/api-docs"));
        session.setAttribute("swagger-allow", GsonUtils.toJson(resources));
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
