package com.flower.sea.commonservice.bo;

import lombok.Data;

/**
 * 权限上传 api类
 *
 * @author zhangLei
 * @serial 2019/11/8
 */
@Data
public class AuthorityApiBO {

    /**
     * api路径
     */
    private String url;

    /**
     * 请求方式
     */
    private String method;
}
