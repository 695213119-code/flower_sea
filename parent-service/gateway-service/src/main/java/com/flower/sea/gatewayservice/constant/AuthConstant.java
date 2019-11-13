package com.flower.sea.gatewayservice.constant;

/**
 * 鉴权常量类
 *
 * @author zhangLei
 * @serial 2019/11/13
 */
public class AuthConstant {

    private AuthConstant() {
    }

    /**
     * 鉴权接口返回参数-map-key
     */
    public final static String AUTHENTICATION_VERIFICATION_RETURN_KEY = "status";

    /**
     * 鉴权验证-此接口需要token验证
     */
    public final static Integer AUTHENTICATION_VERIFICATION_NEED_TOKEN = 1;

    /**
     * 请求头中的token,key值
     */
    public final static String ACCESS_TOKEN = "access_token";

}
