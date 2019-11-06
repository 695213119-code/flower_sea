package com.flower.sea.authservice.controller;

import com.flower.sea.authservice.service.IAuthorityService;
import com.flower.sea.commonservice.recurrence.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 鉴权
 *
 * @author zhangLei
 * @serial 2019/11/6
 */
@RequestMapping("/authority")
@RestController
@Api(tags = "鉴权")
public class AuthorityController {

    private final IAuthorityService authorityService;

    @Autowired
    public AuthorityController(IAuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @PostMapping("/generateUserToken")
    @ApiOperation(value = "生成用户token")
    public ResponseObject generateUserToken(@RequestParam(value = "token过期时间,毫秒值") Long invalidTime,
                                            @RequestParam(value = "用户id") Long userId) {
        return authorityService.generateUserToken(invalidTime, userId);
    }


}

