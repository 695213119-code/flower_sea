package com.flower.sea.userservice.dto.out.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户登录数据返回DTO
 *
 * @author zhangLei
 * @serial 2019/12/8 0:09
 */
@Data
public class UserLoginResponseDTO {

    @ApiModelProperty("用户token")
    private String userToken;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "用户年龄")
    private Integer age;

    @ApiModelProperty(value = "用户性别 1-男 2-女 0-未知")
    private Integer gender;

    @ApiModelProperty(value = "用户生日(农历)")
    private LocalDateTime birth;

    @ApiModelProperty(value = "用户下一次的生日时间(阳历)")
    private LocalDateTime birthAnother;

}