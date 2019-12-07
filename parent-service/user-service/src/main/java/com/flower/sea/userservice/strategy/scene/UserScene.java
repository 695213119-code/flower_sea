package com.flower.sea.userservice.strategy.scene;

import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.userservice.dto.in.UserLoginDTO;
import com.flower.sea.userservice.strategy.business.login.IUserLoginStrategy;
import com.flower.sea.userservice.strategy.factory.UserStrategyFactory;
import org.springframework.stereotype.Service;

/**
 * 用户场景类
 *
 * @author zhangLei
 * @serial 2019/12/7 21:56
 */
@Service
public class UserScene {

    /**
     * 用户登录场景方法
     *
     * @param userLoginDTO 参数类
     * @param key          策略key
     * @return ResponseObject
     */
    public ResponseObject login(UserLoginDTO userLoginDTO, String key) {
        IUserLoginStrategy userLoginStrategy = (IUserLoginStrategy) UserStrategyFactory.getInstance().getStrategy(key);
        return userLoginStrategy.login(userLoginDTO);
    }

}

