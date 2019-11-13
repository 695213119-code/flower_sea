package com.flower.sea.gatewayservice.vo;

import lombok.Data;

/**
 * 验证参数类
 *
 * @author zhangLei
 * @serial 2019/11/13
 */
@Data
public class Gateway {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 具体的请求路径(对应到服务)
     */
    private String url;
}
