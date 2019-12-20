package com.flower.sea.userservice.call.auth;

import com.flower.sea.commonservice.recurrence.ResponseObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 鉴权服务
 *
 * @author zhangLei
 * @serial 2019/11/13
 */
@FeignClient(value = "auth-service", fallback = AuthUserFeignImpl.class)
public interface IAuthUserFeign {

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
