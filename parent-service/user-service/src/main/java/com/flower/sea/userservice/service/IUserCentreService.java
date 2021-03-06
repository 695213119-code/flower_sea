package com.flower.sea.userservice.service;

import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.userservice.dto.in.user.EditUserInfoDTO;
import com.flower.sea.userservice.dto.in.user.ThirdPartyBindingUserDTO;
import com.flower.sea.userservice.dto.in.user.WeChatAppletLoginDTO;
import org.springframework.validation.BindingResult;

/**
 * @author zhangLei
 * @serial 2019/12/4 22:37
 */
public interface IUserCentreService {

    /**
     * 获取微信的openId
     *
     * @param weChatCode 微信的用户code
     * @return ResponseObject
     */
    ResponseObject getWeChatOpenId(String weChatCode);

    /**
     * 第三方绑定用户
     *
     * @param thirdPartyBindingUserDTO 第三方绑定用户参数DTO
     * @param bindingResult            bindingResult
     * @return ResponseObject
     */
    ResponseObject thirdPartyBindingUser(ThirdPartyBindingUserDTO thirdPartyBindingUserDTO, BindingResult bindingResult);

    /**
     * 微信小程序登录
     *
     * @param weChatAppletLoginDTO 微信小程序登录参数DTO
     * @param bindingResult        bindingResult
     * @return ResponseObject
     */
    ResponseObject weChatAppletLogin(WeChatAppletLoginDTO weChatAppletLoginDTO, BindingResult bindingResult);

    /**
     * 根据token获取用户详情
     *
     * @return ResponseObject
     */
    ResponseObject getUserDetails();

    /**
     * 编辑用户信息
     *
     * @param editUserInfoDTO 编辑用户信息参数DTO
     * @return ResponseObject
     */
    ResponseObject editUserInfo(EditUserInfoDTO editUserInfoDTO);
}
