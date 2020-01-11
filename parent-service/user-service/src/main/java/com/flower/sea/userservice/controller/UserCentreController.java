package com.flower.sea.userservice.controller;

import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.startercustomapi.annotation.ApiMenuAnnotation;
import com.flower.sea.startercustomapi.annotation.AuthorityAnnotation;
import com.flower.sea.startercustomapi.annotation.HeaderRequired;
import com.flower.sea.userservice.dto.in.ThirdPartyBindingUserDTO;
import com.flower.sea.userservice.dto.in.WeChatAppletLoginDTO;
import com.flower.sea.userservice.dto.out.user.UserDetailsDTO;
import com.flower.sea.userservice.dto.out.user.UserLoginResponseDTO;
import com.flower.sea.userservice.dto.out.wechat.WechatCallbackDTO;
import com.flower.sea.userservice.service.IUserCentreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhangLei
 * @serial 2019/11/13
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户模块")
@ApiMenuAnnotation(name = "用户模块")
public class UserCentreController {

    private final IUserCentreService userCentreService;

    @Autowired
    public UserCentreController(IUserCentreService userCentreService) {
        this.userCentreService = userCentreService;
    }

    @GetMapping("/getWeChatOpenId")
    @ApiOperation(value = "获取微信的openId", response = WechatCallbackDTO.class)
    @AuthorityAnnotation(isToken = false)
    public ResponseObject getWeChatOpenId(@ApiParam(value = "微信的code", required = true) @RequestParam String weChatCode) {
        return userCentreService.getWeChatOpenId(weChatCode);
    }

    @PostMapping("/weChatAppletLogin")
    @ApiOperation(value = "微信小程序登录", response = UserLoginResponseDTO.class)
    @AuthorityAnnotation(isToken = false)
    public ResponseObject weChatAppletLogin(@ApiParam(value = "微信小程序登录参数类", required = true) @RequestBody @Validated WeChatAppletLoginDTO weChatAppletLoginDTO,
                                            BindingResult bindingResult) {
        return userCentreService.weChatAppletLogin(weChatAppletLoginDTO, bindingResult);
    }

    @PostMapping("/thirdPartyBindingUser")
    @ApiOperation(value = "第三方绑定用户", response = UserLoginResponseDTO.class)
    @AuthorityAnnotation(isToken = false)
    public ResponseObject thirdPartyBindingUser(@ApiParam(value = "第三方绑定用户参数DTO", required = true) @RequestBody @Validated ThirdPartyBindingUserDTO thirdPartyBindingUserDTO,
                                                BindingResult bindingResult) {
        return userCentreService.thirdPartyBindingUser(thirdPartyBindingUserDTO, bindingResult);
    }

    @GetMapping("/getUserDetails")
    @ApiOperation(value = "根据token获取用户详情", response = UserDetailsDTO.class)
    @AuthorityAnnotation(isToken = true)
    @HeaderRequired
    public ResponseObject getUserDetails() {
        return userCentreService.getUserDetails();
    }


}
