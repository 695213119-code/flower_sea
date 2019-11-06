package com.flower.sea.authservice.service.impl;

import com.flower.sea.authservice.service.IAuthorityService;
import com.flower.sea.authservice.utils.JwtUtils;
import com.flower.sea.commonservice.recurrence.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 鉴权实现类
 *
 * @author zhangLei
 * @serial 2019/11/6
 */
@Service
@Slf4j
public class AuthorityServiceImpl implements IAuthorityService {

    @Override
    public ResponseObject generateUserToken(Long invalidTime, Long userId) {
        return ResponseObject.success(JwtUtils.generateToken(userId, invalidTime));
    }
}
