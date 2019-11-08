package com.flower.sea.authservice.monitor;

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
    public void process(String authentication_queue) {
        log.info("-----开始上传权限API配置-----");
        System.out.println("Receiver:" + authentication_queue);
    }
}
