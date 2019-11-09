package com.flower.sea.authservice.monitor;

import com.flower.sea.commonservice.bo.AuthorityAppBO;
import com.flower.sea.commonservice.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 鉴权队列消息监听类
 *
 * @author zhangLei
 * @serial 2019/11/8
 */
@Component
@RabbitListener(queues = "authentication_queue")
@Slf4j
public class AuthorityMessageMonitor {

    @RabbitHandler
    public void process(String authenticationQueue) {
        AuthorityAppBO authorityApp = JsonUtils.json2Object(authenticationQueue, AuthorityAppBO.class);
        log.info("-----开始上传权限API配置====>>本次上传的服务为:[" + authorityApp.getAppName() + "]-----");
        System.out.println("Receiver:" + authenticationQueue);
        log.info("----权限api配置上传结束,上传的api个数为:[10个]----");
    }
}
