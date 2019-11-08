package com.flower.sea.commonservice.annotation;

import java.lang.annotation.*;

/**
 * 自定义权限注解
 *
 * @author zhangLei
 * @serial 2019/11/8
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthorityAnnotation {
}
