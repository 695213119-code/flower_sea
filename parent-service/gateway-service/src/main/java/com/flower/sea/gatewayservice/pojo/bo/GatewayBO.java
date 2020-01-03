package com.flower.sea.gatewayservice.pojo.bo;

import lombok.Data;

/**
 * @author zhangLei
 * @serial 2020-01-03
 */
@Data
public class GatewayBO {
    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 具体的请求路径(对应到服务)
     */
    private String url;
}
