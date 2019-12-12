package com.flower.sea.userservice.dto.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录参数DTO
 *
 * @author zhangLei
 * @serial 2019/12/6 14:14
 */
@Data
public class UserLoginDTO {

    @ApiModelProperty("登录平台 1-PC 2-APP 3-小程序")
    @NotBlank(message = "登录平台不能为空")
    private Integer loginPlatform;

    @ApiModelProperty("登录方式 " +
            "1-账号密码登录 " +
            "2-短信验证码登录 " +
            "3-微信小程序的登录,必传参数[wechatCode或者openId]")
    @NotBlank(message = "登录方式不能为空")
    private Integer loginType;

    @ApiModelProperty("微信的Code")
    private String wechatCode;

    @ApiModelProperty("微信的openId")
    private String openId;

    @ApiModelProperty("手机号/账号")
    private String phone;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("短信验证码")
    private String shortCode;


}
