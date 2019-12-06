package com.flower.sea.userservice.dto.out;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信的回调DTO
 *
 * @author zhangLei
 * @serial 2019/12/5 9:14
 */
@Data
public class WechatCallbackDTO {


    @ApiModelProperty("微信回调的session_key")
    private String sessionKey;

    @ApiModelProperty("微信回调的openId")
    private String openId;

}
