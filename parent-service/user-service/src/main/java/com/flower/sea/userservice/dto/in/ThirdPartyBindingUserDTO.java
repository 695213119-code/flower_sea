package com.flower.sea.userservice.dto.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 第三方绑定用户DTO
 *
 * @author zhangLei
 * @serial 2019/12/12 14:07
 */
@Data
public class ThirdPartyBindingUserDTO implements Serializable {

    @ApiModelProperty(value = "绑定的平台类型(1-QQ 2-微信)")
    @NotNull(message = "平台类型不能为空")
    private Integer platform;

    @ApiModelProperty("第三方的唯一标识")
    @NotBlank(message = "第三方的唯一标识不能为空")
    private String unionId;

    @ApiModelProperty(value = "用户手机号")
    @NotBlank(message = "用户手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "第三方用户性别(0-未知 1-男 2女)")
    private Integer gender;

    @ApiModelProperty(value = "第三方用户昵称")
    private String nickName;

    @ApiModelProperty(value = "第三方用户年龄")
    private Integer age;

    @ApiModelProperty(value = "第三方用户头像")
    private String avatar;

    @ApiModelProperty(value = "第三方用户的城市")
    private String city;

    @ApiModelProperty(value = "第三方用户的国家")
    private String country;

    @ApiModelProperty(value = "第三方用户的使用语言")
    private String language;

    @ApiModelProperty(value = "第三方用户的省份")
    private String province;
}
