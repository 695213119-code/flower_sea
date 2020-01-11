package com.flower.sea.interfaceservice.authentication;

import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.interfaceservice.authentication.fallback.AuthorityCallInterfaceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 鉴权服务接口模块
 *
 * @author zhangLei
 * @serial 2020-01-03
 */
@FeignClient(value = "auth-service", fallback = AuthorityCallInterfaceImpl.class)
public interface IAuthorityCallInterface {

    String API_AUTH = "/api/authority";

    /**
     * 获取token
     *
     * @param invalidTime token过期时间,毫秒值
     * @param userId      用户id
     * @return ResponseObject
     */
    @GetMapping(API_AUTH + "/generateUserToken")
    ResponseObject<String> generateUserToken(@RequestParam(value = "invalidTime") Long invalidTime,
                                             @RequestParam(value = "userId") Long userId);

    /**
     * 验证该请求是否需要验证
     *
     * @param serviceName 服务名称
     * @param url         请求的url
     * @return ResponseObject
     */
    @PostMapping(API_AUTH + "/verificationIsToken")
    ResponseObject verificationIsToken(@RequestParam(value = "serviceName") String serviceName,
                                       @RequestParam(value = "url") String url);

    /**
     * 校验token的合法性
     *
     * @param userToken 用户token
     * @return ResponseObject
     */
    @GetMapping(API_AUTH + "/verificationTokenIsCorrect")
    ResponseObject verificationTokenIsCorrect(@RequestParam(value = "userToken") String userToken);

}
