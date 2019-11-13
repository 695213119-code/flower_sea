package com.flower.sea.gatewayservice.call.auth;

import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.gatewayservice.vo.Gateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhangLei
 * @serial 2019/11/13
 */
@Component
@Slf4j
public class AuthCallImpl implements IAuthCall {

    @Override
    public ResponseObject verificationIsToken(Gateway gateway) {
        log.error("调用鉴权服务[auth-service]接口[verificationIsToken]失败,这里降级处理,缺省值为null");
        return null;
    }

    @Override
    public ResponseObject verificationTokenIsCorrect(String userToken) {
        log.error("调用鉴权服务[auth-service]接口[verificationTokenIsCorrect]失败,这里降级处理,缺省值为null");
        return null;
    }
}
