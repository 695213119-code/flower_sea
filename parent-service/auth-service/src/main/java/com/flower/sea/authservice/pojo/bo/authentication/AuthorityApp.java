package com.flower.sea.authservice.pojo.bo.authentication;

import lombok.Data;

import java.util.List;

/**
 * 权限上传 app类
 *
 * @author zhangLei
 * @serial 2019/11/8
 */
@Data
public class AuthorityApp {

    /**
     * 项目名称
     */
    private String appName;

    /**
     * 项目说明
     */
    private String appExplain;

    /**
     * 菜单集合
     */
    private List<AuthorityMenu> authorityMenu;


}
