package com.flower.sea.userservice.strategy.business.login;

import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.userservice.dto.in.UserLoginDTO;

/**
 * 用户策略抽象接口
 *
 * @author zhangLei
 * @serial 2019/12/7 21:59
 */
public interface IUserLoginStrategy {

    /**
     * 用户登录
     *
     * @param userLoginDTO 参数类
     * @return ResponseObject
     */
    ResponseObject login(UserLoginDTO userLoginDTO);
}
