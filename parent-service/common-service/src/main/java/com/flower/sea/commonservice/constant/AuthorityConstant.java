package com.flower.sea.commonservice.constant;

/**
 * 权限常量类
 *
 * @author zhangLei
 * @serial 2019/11/13
 */
public class AuthorityConstant {

    private AuthorityConstant() {

    }

    /**
     * 请求头中的token,key值
     */
    public final static String AUTHORITY_ACCESS_TOKEN = "access_token";

    /**
     * 鉴权验证-此接口需要token验证
     */
    public final static Integer AUTHENTICATION_VERIFICATION_NEED_TOKEN = 1;

    /**
     * 鉴权验证-此接口不需要token验证
     */
    public final static Integer AUTHENTICATION_VERIFICATION_NOT_NEED_TOKEN = 2;

    /**
     * 鉴权接口返回的字段名称-status
     */
    public final static String AUTHENTICATION_VERIFICATION_RESULT_KEY = "status";

}
