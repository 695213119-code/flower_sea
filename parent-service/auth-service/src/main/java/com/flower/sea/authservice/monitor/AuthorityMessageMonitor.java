package com.flower.sea.authservice.monitor;

import com.flower.sea.authservice.service.IAuthorityService;
import com.flower.sea.commonservice.utils.JsonUtils;
import com.flower.sea.authservice.pojo.bo.authentication.AuthorityApp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 队列消息监听-上传鉴权API
 *
 * @author zhangLei
 * @serial 2019/11/8
 */
@Component
@RabbitListener(queues = "authentication_queue")
@Slf4j
public class AuthorityMessageMonitor {

    private final IAuthorityService authorityService;

    @Autowired
    public AuthorityMessageMonitor(IAuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @RabbitHandler
    public void process(String authenticationQueue) {
        AuthorityApp authorityApp = JsonUtils.json2Object(authenticationQueue, AuthorityApp.class);
        log.info("-----开始上传权限API配置===>>本次上传的服务为:[" + authorityApp.getAppName() + "]");
        Integer count = authorityService.uploadAuthApi(authorityApp);
        log.info("-----权限api配置上传结束,上传的api个数为:[" + count + "个]");
    }
}
