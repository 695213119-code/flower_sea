package com.flower.sea.userservice.dto.out.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统一 用户登录后的返回数据
 *
 * @author zhangLei
 * @serial 2019/12/8 0:09
 */
@Data
public class UserLoginResponseDTO {

    @ApiModelProperty("用户token")
    private String userToken;

}
