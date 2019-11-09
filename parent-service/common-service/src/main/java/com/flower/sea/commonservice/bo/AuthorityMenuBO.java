package com.flower.sea.commonservice.bo;

import lombok.Data;

import java.util.List;

/**
 * 权限上传 menu类
 *
 * @author zhangLei
 * @serial 2019/11/8
 */
@Data
public class AuthorityMenuBO {

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
    private List<AuthorityApiBO> authorityApi;
}