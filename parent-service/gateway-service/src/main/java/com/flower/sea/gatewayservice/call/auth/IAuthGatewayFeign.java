package com.flower.sea.gatewayservice.call.auth;

import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.gatewayservice.vo.Gateway;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 鉴权服务
 *
 * @author zhangLei
 * @serial 2019/11/13
 */
@FeignClient(value = "auth-service", fallback = AuthGatewayFeignImpl.class)
public interface IAuthGatewayFeign {

    String API_AUTH = "/api/authority";

    /**
     * 验证该请求是否需要验证
     *
     * @param gateway 参数类
     * @return ResponseObject
     */
    @PostMapping(API_AUTH + "/verificationIsToken")
    ResponseObject verificationIsToken(Gateway gateway);

    /**
     * 校验token的合法性
     *
     * @param userToken 用户token
     * @return ResponseObject
     */
    @GetMapping(API_AUTH + "/verificationTokenIsCorrect")
    ResponseObject verificationTokenIsCorrect(@RequestParam(value = "userToken") String userToken);
}
