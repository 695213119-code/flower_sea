package com.flower.sea.gatewayservice.utils;

import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;

/**
 * HttpServletRequest工具类
 *
 * @author zhangLei
 * @serial 2019/11/6
 */
public class HttpServletRequestUtils {

    private static HttpServletRequest httpServletRequest = RequestContext.getCurrentContext().getRequest();

    private HttpServletRequestUtils() {
    }

    private static HttpServletRequest getInstance() {
        if (null == httpServletRequest) {
            httpServletRequest = RequestContext.getCurrentContext().getRequest();
        }
        return httpServletRequest;
    }

    /**
     * 获取请求路径
     *
     * @return String
     */
    public static String getRequestUrl() {
        return getInstance().getRequestURI();
    }
}

