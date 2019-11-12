package com.flower.sea.coreservice.controller;


import com.flower.sea.startercustomapi.annotation.ApiMenuAnnotation;
import com.flower.sea.startercustomapi.annotation.AuthorityAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典
 *
 * @author zhangLei
 * @serial 2019/11/6
 */
@RequestMapping("/api/dictionaries")
@RestController
@Api(tags = "字典")
@ApiMenuAnnotation(name = "字典模块")
public class DictionariesController {


    @PostMapping("/test")
    @ApiOperation("test")
    @AuthorityAnnotation
    public String test() {
        return "";
    }


    @PostMapping("/ceshi")
    @ApiOperation("ceshi")
    public String ceshi() {
        return "";
    }
}
