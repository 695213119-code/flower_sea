package com.flower.sea.authservice.pojo.dto.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * jwt的解析token返回类
 *
 * @author zhangLei
 * @serial 2019/11/8
 */
@Data
@Builder
public class JwtTokenOut {

    @ApiModelProperty("解析出来的用户id")
    private Long userId;

    @ApiModelProperty("token的创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime crateTime;

    @ApiModelProperty("token的过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

}
