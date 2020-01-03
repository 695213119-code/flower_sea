package com.flower.sea.authservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 鉴权服务
 *
 * @author zhangLei
 * @serial 2019/11/6
 */
@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
@MapperScan(value = {"com.flower.sea.authservice.auth.mapper","com.flower.sea.authservice.mapper"})
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

}
