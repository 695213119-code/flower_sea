package com.flower.sea.authservice.constant;

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
     * 鉴权-需要token验证
     */
    public static Integer AUTH_NEED_TOKEN = 1;

    /**
     * 鉴权-不需要token验证
     */
    public static Integer AUTH_NOT_NEED_TOKEN = 2;
}
