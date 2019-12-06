package com.flower.sea.userservice.strategy.key;

/**
 * 用户模块的策略key
 *
 * @author zhangLei
 * @serial 2019/12/6 17:13
 */
public class UserStrategyKey {

    private UserStrategyKey() {

    }

    /**
     * 登录平台-pc
     */
    public static final String LOGIN_PLATFORM_PC = "login_platform_pc";

    /**
     * 登录平台-app
     */
    public static final String LOGIN_PLATFORM_APP = "login_platform_app";

    /**
     * 登录平台-小程序
     */
    public static final String LOGIN_PLATFORM_SMALL_PROGRAM = "login_platform_small_program";

    /**
     * 登录方式-账号加密码
     */
    public static final String LOGIN_TYPE_ACCOUNT_AND_PASSWORD = "login_type_account_and_password";

    /**
     * 登录方式-微信的code
     */
    public static final String LOGIN_TYPE_WECHAT_CODE = "login_type_wechat_code";

    /**
     * 登录方式-手机短信验证码
     */
    public static final String LOGIN_TYPE_PHONE_SHORT_CODE = "login_type_phone_short_code";


}
