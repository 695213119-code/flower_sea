package com.flower.sea.userservice.controller;

import com.flower.sea.startercustomapi.annotation.ApiMenuAnnotation;
import com.flower.sea.startercustomapi.annotation.AuthorityAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangLei
 * @serial 2019/11/13
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户")
@ApiMenuAnnotation(name = "用户模块")
public class UserController {


    @PostMapping("/login")
    @ApiOperation("用户登录")
    @AuthorityAnnotation(isToken = false)
    public String login() {
        return "登录成功!";
    }

}
