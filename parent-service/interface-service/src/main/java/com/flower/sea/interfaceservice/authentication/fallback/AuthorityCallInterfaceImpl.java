package com.flower.sea.interfaceservice.authentication.fallback;

import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.interfaceservice.authentication.IAuthorityCallInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 鉴权熔断fallback类
 *
 * @author zhangLei
 * @serial 2020-01-03
 */
@Component
@Slf4j
public class AuthorityCallInterfaceImpl implements IAuthorityCallInterface {

    @Override
    public ResponseObject<String> generateUserToken(Long invalidTime, Long userId) {
        log.error("调用服务[鉴权服务-->auth-service],接口[生成用户token-->generateUserToken]失败,入参:[invalidTime:{},userId:{}]", invalidTime, userId);
        return null;
    }

    @Override
    public ResponseObject verificationIsToken(String serviceName, String url) {
        log.error("调用服务[鉴权服务-->auth-service],接口[校验接口是否需要token验证-->verificationIsToken]失败,入参:[serviceName:{},url:{}]", serviceName, url);
        return null;
    }

    @Override
    public ResponseObject verificationTokenIsCorrect(String userToken) {
        log.error("调用服务[鉴权服务-->auth-service],接口[校验用户token是否合法-->verificationTokenIsCorrect]失败,入参[userToken:{}]", userToken);
        return null;
    }
}
