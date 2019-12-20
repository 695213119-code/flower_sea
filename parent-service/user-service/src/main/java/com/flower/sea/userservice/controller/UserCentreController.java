package com.flower.sea.userservice.controller;

import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.startercustomapi.annotation.ApiMenuAnnotation;
import com.flower.sea.startercustomapi.annotation.AuthorityAnnotation;
import com.flower.sea.userservice.dto.in.ThirdPartyBindingUserDTO;
import com.flower.sea.userservice.dto.in.UserLoginDTO;
import com.flower.sea.userservice.dto.out.user.UserLoginResponseDTO;
import com.flower.sea.userservice.dto.out.wechat.WechatCallbackDTO;
import com.flower.sea.userservice.service.IUserCentreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseObject getWeChatOpenId(@ApiParam(value = "微信的Code", required = true) @RequestParam String weChatCode) {
        return userCentreService.getWeChatOpenId(weChatCode);
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录", response = UserLoginResponseDTO.class)
    @AuthorityAnnotation(isToken = false)
    public ResponseObject login(@ApiParam(value = "用户登录参数DTO", required = true) @RequestBody UserLoginDTO userLoginDTO) {
        return userCentreService.login(userLoginDTO);
    }

    @PostMapping("/thirdPartyBindingUser")
    @ApiOperation(value = "第三方绑定用户", response = UserLoginResponseDTO.class)
    @AuthorityAnnotation(isToken = false)
    public ResponseObject thirdPartyBindingUser(@ApiParam(value = "第三方绑定用户参数DTO", required = true) @RequestBody ThirdPartyBindingUserDTO thirdPartyBindingUserDTO) {
        return userCentreService.thirdPartyBindingUser(thirdPartyBindingUserDTO);
    }


}
