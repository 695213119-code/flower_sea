package com.flower.sea.authservice.pojo.bo.verification;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 验证token的参数类
 *
 * @author zhangLei
 * @serial 2019/11/13
 */
@Data
public class Gateway {

    /**
     * 服务名称
     */
    @NotBlank(message = "服务名称[serviceName]不能为空")
    private String serviceName;

    /**
     * 请求路径
     */
    @NotBlank(message = "请求路径[url]不能为空")
    private String url;
}
