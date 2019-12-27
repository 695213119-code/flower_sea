package com.flower.sea.authservice.controller;

import com.flower.sea.authservice.pojo.bo.verification.Gateway;
import com.flower.sea.authservice.service.IAuthorityService;
import com.flower.sea.commonservice.recurrence.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 鉴权
 *
 * @author zhangLei
 * @serial 2019/11/6
 */
@RequestMapping("/api/authority")
@RestController
@Api(tags = "鉴权")
public class AuthorityController {

    private final IAuthorityService authorityService;

    @Autowired
    public AuthorityController(IAuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping("/generateUserToken")
    @ApiOperation(value = "生成用户token")
    public ResponseObject<String> generateUserToken(@ApiParam(value = "token过期时间,毫秒值", required = true) @RequestParam Long invalidTime,
                                            @ApiParam(value = "用户id", required = true) @RequestParam Long userId) {
        return authorityService.generateUserToken(invalidTime, userId);
    }

    @GetMapping("/analysisUserToken")
    @ApiOperation(value = "解析用户token")
    public ResponseObject analysisUserToken(@ApiParam(value = "用户token", required = true) @RequestParam String userToken) {
        return authorityService.analysisUserToken(userToken);
    }

    @PostMapping("/verificationIsToken")
    @ApiOperation(value = "校验接口是否需要token验证")
    public ResponseObject verificationIsToken(@ApiParam(value = "验证token参数类", required = true) @Valid @RequestBody Gateway gateway,
                                              BindingResult bindingResult) {
        return authorityService.verificationIsToken(gateway, bindingResult);
    }

    @GetMapping("/verificationTokenIsCorrect")
    @ApiOperation(value = "校验用户token是否合法")
    public ResponseObject verificationTokenIsCorrect(@ApiParam(value = "用户token", required = true) @RequestParam String userToken) {
        return authorityService.verificationTokenIsCorrect(userToken);
    }


}

