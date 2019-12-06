package com.flower.sea.userservice.service;

import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.userservice.dto.in.UserLoginDTO;

/**
 * @author zhangLei
 * @serial 2019/12/4 22:37
 */
public interface IUserCentreService {

    /**
     * 获取微信的openId
     *
     * @param wechatCode 微信的用户code
     * @return ResponseObject
     */
    ResponseObject getWechatOpenId(String wechatCode);

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录参数DTO
     * @return ResponseObject
     */
    ResponseObject login(UserLoginDTO userLoginDTO);
}
