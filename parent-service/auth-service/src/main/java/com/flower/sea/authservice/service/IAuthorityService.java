package com.flower.sea.authservice.service;

import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.entityservice.upload.api.AuthorityApp;

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
    ResponseObject generateUserToken(Long invalidTime, Long userId);

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
}
