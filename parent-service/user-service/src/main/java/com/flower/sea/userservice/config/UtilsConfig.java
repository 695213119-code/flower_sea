package com.flower.sea.userservice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 对common.utils包下的类进行注入
 *
 * @author zhangLei
 * @serial 2019/12/8 0:58
 */
@ComponentScan("com.flower.sea.commonservice.utils")
@Configuration
public class UtilsConfig {

}
