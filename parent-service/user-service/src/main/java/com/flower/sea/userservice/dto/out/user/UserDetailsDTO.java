package com.flower.sea.userservice.dto.out.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户详情DTO
 *
 * @author zhangLei
 * @serial 2020-01-11
 */
@Data
public class UserDetailsDTO {

    @ApiModelProperty("用户手机号")
    private String phone;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户年龄")
    private Integer age;

    @ApiModelProperty("用户性别 1-男 2-女 0-未知")
    private Integer gender;

    @ApiModelProperty("用户生日(农历)")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime birth;

    @ApiModelProperty("用户下一次生日时间(阳历)")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime birthAnother;

    @ApiModelProperty("用户邮箱")
    private String email;
}
