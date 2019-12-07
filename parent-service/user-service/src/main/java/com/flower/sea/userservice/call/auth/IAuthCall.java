package com.flower.sea.userservice.call.auth;

import com.flower.sea.commonservice.recurrence.ResponseObject;
import feign.Param;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 鉴权服务
 *
 * @author zhangLei
 * @serial 2019/11/13
 */
@FeignClient(value = "auth-service", fallback = AuthCallImpl.class)
@Service
public interface IAuthCall {

    String API_AUTH = "/api/authority";

    /**
     * 获取token
     *
     * @param invalidTime token过期时间,毫秒值
     * @param userId      用户id
     * @return ResponseObject
     */
    @GetMapping(API_AUTH + "/generateUserToken")
    ResponseObject generateUserToken(@RequestParam(value = "invalidTime") Long invalidTime,
                                     @RequestParam(value = "userId") Long userId);


}
