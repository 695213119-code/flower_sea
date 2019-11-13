package com.flower.sea.authservice.pojo.dto.out;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 鉴权返回类
 *
 * @author zhangLei
 * @serial 2019/11/13
 */
@Data
public class AuthOut {

    @ApiModelProperty("1-需要验证 2-不需要验证")
    private Integer status;
}
