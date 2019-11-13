package com.flower.sea.gatewayservice.utils;

import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangLei
 * @serial 2019/11/13
 */
public class RequestUtils {

    private RequestUtils() {

    }

    private static RequestContext currentContext;

    private static RequestContext getInstance() {
        if (null == currentContext) {
            currentContext = RequestContext.getCurrentContext();
        }
        return currentContext;
    }


    public static String getRequestUrl() {
        return getInstance().getRequest().getRequestURI();
    }


    public static RequestContext getCurrentContext() {
        return getInstance();
    }

    public static HttpServletRequest getRequest() {
        return getInstance().getRequest();
    }

}
