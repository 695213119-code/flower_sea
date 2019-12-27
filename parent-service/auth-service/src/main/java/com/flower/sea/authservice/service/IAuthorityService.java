package com.flower.sea.authservice.service;

import com.flower.sea.authservice.pojo.bo.verification.Gateway;
import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.authservice.pojo.bo.authentication.AuthorityApp;
import org.springframework.validation.BindingResult;

/**
 * 权限
 *
 * @author zhangLei
 * @serial 2019/11/6
 */
public interface IAuthorityService {


    /**
     * 生成用户token
     *
     * @param invalidTime token过期时间
     * @param userId      用户id
     * @return ResponseObject
     */
    ResponseObject<String> generateUserToken(Long invalidTime, Long userId);

    /**
     * 校验用户token
     *
     * @param userToken 用户token
     * @return ResponseObject
     */
    ResponseObject analysisUserToken(String userToken);


    /**
     * 上传权限API
     *
     * @param authorityApp 权限api数据
     * @return ResponseObject
     */
    Integer uploadAuthApi(AuthorityApp authorityApp);

    /**
     * 校验接口是否需要token验证
     *
     * @param gateway       验证token参数类
     * @param bindingResult bindingResult
     * @return ResponseObject
     */
    ResponseObject verificationIsToken(Gateway gateway, BindingResult bindingResult);

    /**
     * 校验用户token是否合法
     *
     * @param userToken 用户token
     * @return ResponseObject
     */
    ResponseObject verificationTokenIsCorrect(String userToken);
}
