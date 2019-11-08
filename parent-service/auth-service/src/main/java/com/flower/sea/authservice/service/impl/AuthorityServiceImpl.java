package com.flower.sea.authservice.service.impl;

import com.flower.sea.authservice.pojo.bo.JwtTokenBO;
import com.flower.sea.authservice.pojo.dto.out.JwtTokenOut;
import com.flower.sea.authservice.service.IAuthorityService;
import com.flower.sea.authservice.utils.JwtUtils;
import com.flower.sea.commonservice.recurrence.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

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

    @Override
    public ResponseObject analysisUserToken(String userToken) {
        ResponseObject<JwtTokenBO> validTokenResponse = JwtUtils.analysisToken(userToken);
        if (HttpStatus.OK.value() != validTokenResponse.getCode()) {
            return validTokenResponse;
        }
        JwtTokenBO jwtToken = validTokenResponse.getData();
        return ResponseObject.success(JwtTokenOut.builder().userId(jwtToken.getUid())
                .crateTime(LocalDateTime.ofEpochSecond(jwtToken.getSta() / 1000, 0, ZoneOffset.ofHours(8)))
                .expireTime(LocalDateTime.ofEpochSecond(jwtToken.getExp() / 1000, 0, ZoneOffset.ofHours(8)))
                .build());
    }
}
