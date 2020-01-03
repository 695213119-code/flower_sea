package com.flower.sea.gatewayservice.utils;

import com.flower.sea.gatewayservice.pojo.bo.GatewayBO;

/**
 * 网关工具类
 *
 * @author zhangLei
 * @serial 2019/11/13
 */
public class GatewayUtils {

    private static final String BACKSLASH = "/";
    
    /**
     * 根据网关请求路径获取对应的参数
     *
     * @param requestUrl 请求路径
     * @return Gateway
     */
    public static GatewayBO getGateway(String requestUrl) {
        GatewayBO gateway = new GatewayBO();
        String[] requestUrlArray = requestUrl.split(BACKSLASH);
        String serviceName = requestUrlArray[2];
        gateway.setServiceName(serviceName);
        gateway.setUrl(requestUrl.substring(requestUrl.lastIndexOf(serviceName) + serviceName.length()));
        return gateway;
    }

}
