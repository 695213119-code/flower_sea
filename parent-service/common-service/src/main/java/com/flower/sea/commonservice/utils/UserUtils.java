package com.flower.sea.commonservice.utils;

import com.flower.sea.commonservice.exception.BusinessException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 用户工具类
 *
 * @author zhangLei
 * @serial 2020-01-11
 */
@Slf4j
@Component
public class UserUtils {

    private final RedisUtils redisUtils;
    private final TokenUtils tokenUtils;

    @Autowired
    public UserUtils(RedisUtils redisUtils, TokenUtils tokenUtils) {
        this.redisUtils = redisUtils;
        this.tokenUtils = tokenUtils;
    }

    @Data
    private static class User {
        private Long id;
        private String phone;
        private String userName;
        private String password;
        private Long roleId;
        private Integer status;
        private Long createBy;
        private Date createTime;
        private Long updateBy;
        private Date updateTime;
        private Integer isDeleted;
    }

    public User getUser() {
        String userRedisValue = redisUtils.get(tokenUtils.getToken());
        if (StringUtils.isBlank(userRedisValue)) {
            throw new BusinessException("User information not obtained!");
        }
        User user = JsonUtils.json2Object(userRedisValue, User.class);
        if (null == user || null == user.getId()) {
            throw new BusinessException("Bad user information obtained!");
        }
        return user;
    }

    public Long getUserId() {
        return getUser().getId();
    }

}
