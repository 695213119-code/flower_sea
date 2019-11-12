package com.flower.sea.authservice.pojo.bo.authentication;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限上传 menu类
 *
 * @author zhangLei
 * @serial 2019/11/8
 */
@Data
public class AuthorityMenu {

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 类名称
     */
    private String className;

    /**
     * api 集合
     */
    private List<AuthorityApi> authorityApi;


    public void addApi(AuthorityApi authApi) {
        if (CollUtil.isNotEmpty(authorityApi)) {
            authorityApi.add(authApi);
        } else {
            authorityApi = new ArrayList<>();
            authorityApi.add(authApi);
        }
    }
}
