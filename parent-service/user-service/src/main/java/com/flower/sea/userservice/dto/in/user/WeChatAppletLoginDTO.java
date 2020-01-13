package com.flower.sea.userservice.dto.in.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 微信小程序登录参数DTO
 *
 * @author zhangLei
 * @serial 2019/12/6 14:14
 */
@Data
public class WeChatAppletLoginDTO {

    @ApiModelProperty("微信的openId")
    @NotBlank(message = "openId不能为空")
    private String weChatOpenId;

    @ApiModelProperty(value = "微信用户昵称")
    private String nickName;

    @ApiModelProperty(value = "微信用户年龄")
    private Integer age;

    @ApiModelProperty(value = "微信用户性别(0-未知 1-男 2女)")
    private Integer gender;

    @ApiModelProperty(value = "微信用户头像")
    private String avatar;

    @ApiModelProperty(value = "微信用户的城市")
    private String city;

    @ApiModelProperty(value = "微信用户的国家")
    private String country;

    @ApiModelProperty(value = "微信用户的使用语言")
    private String language;

    @ApiModelProperty(value = "微信用户的省份")
    private String province;
}
