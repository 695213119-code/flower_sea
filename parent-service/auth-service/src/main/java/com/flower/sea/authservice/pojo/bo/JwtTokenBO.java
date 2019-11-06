package com.flower.sea.authservice.pojo.bo;

import lombok.Data;

/**
 * jwt的token信息封装类
 *
 * @author zhangLei
 * @serial 2019/11/6
 */
@Data
public class JwtTokenBO {

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 生成时间
     */
    private Long sta;

    /**
     * 过期时间
     */
    private Long exp;
}
