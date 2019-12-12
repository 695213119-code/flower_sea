package com.flower.sea.userservice.service;

import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.userservice.dto.in.ThirdPartyBindingUserDTO;
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

    /**
     * 用户登录公共封装方法
     *
     * @param userId        用户id
     * @param loginPlatform 用户登录平台
     * @return ResponseObject
     */
    ResponseObject userLoginEncapsulation(Long userId, Integer loginPlatform);

    /**
     * 第三方绑定用户
     *
     * @param thirdPartyBindingUserDTO 第三方绑定用户参数DTO
     * @return ResponseObject
     */
    ResponseObject thirdPartyBindingUser(ThirdPartyBindingUserDTO thirdPartyBindingUserDTO);
}
