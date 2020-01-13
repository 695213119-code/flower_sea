package com.flower.sea.userservice.dto.in.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 编辑用户信息参数DTO
 *
 * @author zhangLei
 * @serial 2020-01-13
 */
@Data
public class EditUserInfoDTO {

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("性别")
    private Integer gender;

    @ApiModelProperty("生日(农历) 格式:yyyy-MM-dd")
    private String birth;

    @ApiModelProperty("头像")
    private String avatar;


}
