package com.flower.sea.userservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信的配置文件读取类
 *
 * @author zhangLei
 * @serial 2019/12/4 23:02
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WeChatProperties {

    /**
     * 微信的appId
     */
    private String appId;

    /**
     * 微信的appSecret
     */
    private String appSecret;


}
