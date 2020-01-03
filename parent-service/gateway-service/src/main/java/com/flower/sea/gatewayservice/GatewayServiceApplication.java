package com.flower.sea.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 网关
 * 此处扫描interface模块的feign接口
 *
 * @author zhangLei
 * @serial 2019-11-5
 */
@SpringBootApplication(scanBasePackages = {"com.flower.sea.gatewayservice","com.flower.sea.interfaceservice"})
@EnableZuulProxy
@EnableEurekaClient
@EnableFeignClients
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

}
