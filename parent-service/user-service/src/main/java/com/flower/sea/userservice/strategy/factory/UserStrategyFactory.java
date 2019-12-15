package com.flower.sea.userservice.strategy.factory;

import com.flower.sea.userservice.strategy.business.login.WechatSmallProgramLogin;
import com.flower.sea.userservice.strategy.key.UserStrategyKey;
import com.flower.sea.userservice.utils.SpringContextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户策略工厂
 *
 * @author zhangLei
 * @serial 2019/12/6 17:47
 */
public class UserStrategyFactory {

    private static Map<String, Object> userStrategyFactoryMap = new HashMap<>(16);

    static {
        //微信小程序登录
        WechatSmallProgramLogin wechatSmallProgramLogin = SpringContextUtils.getBean(WechatSmallProgramLogin.class);
        userStrategyFactoryMap.put(UserStrategyKey.LOGIN_PLATFORM_SMALL_PROGRAM + UserStrategyKey.LOGIN_TYPE_WE_CHAT_CODE, wechatSmallProgramLogin);


    }

    /**
     * 使用静态内部类实现单例模式
     */
    private static class UserStrategyFactoryInside {
        private static UserStrategyFactory userStrategyFactory = new UserStrategyFactory();
    }

    /**
     * 获取实例
     *
     * @return UserStrategyFactory
     */
    public static UserStrategyFactory getInstance() {
        return UserStrategyFactoryInside.userStrategyFactory;
    }

    /**
     * 获取策略的实现
     *
     * @param strategyKey 策略key
     * @return Object
     */
    public Object getStrategy(String strategyKey) {
        return userStrategyFactoryMap.get(strategyKey);
    }


}
