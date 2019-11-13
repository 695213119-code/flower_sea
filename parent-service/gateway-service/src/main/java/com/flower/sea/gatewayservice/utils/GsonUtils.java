package com.flower.sea.gatewayservice.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.List;

/**
 * Gson工具类
 *
 * @author zhangLei
 * @serial 2019/11/13
 */
public class GsonUtils {

    private GsonUtils() {
    }

    private static Gson gson;

    private static Gson getInstance() {
        if (null == gson) {
            gson = new Gson();
        }
        return gson;
    }

    public static String toJson(Object object) {
        return getInstance().toJson(object);
    }

    public static List<SwaggerResource> getSwaggerResources(String string) {
        return getInstance().fromJson(string, new TypeToken<List<SwaggerResource>>() {
        }.getType());
    }


}
