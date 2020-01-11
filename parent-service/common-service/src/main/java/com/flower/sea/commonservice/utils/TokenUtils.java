package com.flower.sea.commonservice.utils;

import com.flower.sea.commonservice.constant.AuthorityConstant;
import com.flower.sea.commonservice.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取token工具类
 *
 * @author zhangLei
 * @serial 2020-01-11
 */
@Component
@Slf4j
class TokenUtils {

    private final HttpServletRequest httpServletRequest;

    @Autowired
    private TokenUtils(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    String getToken() {
        String token;
        token = httpServletRequest.getHeader(AuthorityConstant.AUTHORITY_ACCESS_TOKEN);
        if (StringUtils.isBlank(token)) {
            token = httpServletRequest.getParameter(AuthorityConstant.AUTHORITY_ACCESS_TOKEN);
        }
        if (StringUtils.isBlank(token)) {
            throw new BusinessException("未获取到用户token!");
        }
        log.info("获取到的token为:{}", token);
        return token;
    }
}
