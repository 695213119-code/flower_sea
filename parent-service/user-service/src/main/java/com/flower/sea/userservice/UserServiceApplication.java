package com.flower.sea.userservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 用户
 *
 * @author zhangLei
 * @serial 2019/11/13
 */
@SpringBootApplication(scanBasePackages = {"com.flower.sea.userservice","com.flower.sea.interfaceservice"})
@EnableEurekaClient
@EnableHystrix
@EnableAsync
@MapperScan(value = {"com.flower.sea.userservice.user.mapper","com.flower.sea.userservice.mapper"})
@EnableFeignClients(basePackages = {"com.flower.sea.interfaceservice"})
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
