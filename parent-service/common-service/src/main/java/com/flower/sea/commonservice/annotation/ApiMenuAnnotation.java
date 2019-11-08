package com.flower.sea.commonservice.annotation;

import java.lang.annotation.*;

/**
 * 自定义菜单注解
 *
 * @author zhangLei
 * @serial 2019/11/8
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.TYPE)
public @interface ApiMenuAnnotation {

    String name() default "";
}
