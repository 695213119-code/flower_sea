package com.flower.sea.authservice.pojo.bo.verification;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 验证token的BO类
 *
 * @author zhangLei
 * @serial 2019/11/13
 */
@Data
public class GatewayBO {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 请求路径
     */
    private String url;

    public GatewayBO(String serviceName, String url) {
        this.serviceName = serviceName;
        this.url = url;
    }
}
