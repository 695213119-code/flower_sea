package com.flower.sea.userservice.call.auth;

import com.flower.sea.commonservice.recurrence.ResponseObject;
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
    public ResponseObject generateUserToken(Long invalidTime, Long userId) {
        log.error("调用服务[auth-service],接口[generateUserToken]失败");
        return null;
    }


}
