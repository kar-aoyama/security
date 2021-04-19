package com.lzl.security.annotation;

import jdk.jfr.Registered;

import java.lang.annotation.*;

/**
 * @author lzl
 * @ClassName WhetherLogin
 * @date: 2021/4/13 下午2:25
 * @Description:
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WhetherLogin {

    boolean login() default false;
}
