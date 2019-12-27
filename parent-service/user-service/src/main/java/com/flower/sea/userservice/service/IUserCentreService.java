package com.flower.sea.userservice.service;

import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.userservice.dto.in.ThirdPartyBindingUserDTO;
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
     * @param weChatOpenId 微信的openId
     * @return ResponseObject
     */
    ResponseObject weChatAppletLogin(String weChatOpenId);
}
