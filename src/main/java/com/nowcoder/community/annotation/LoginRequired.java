package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/8 下午 08:00
 * @description 自定义注解 判断是否是登陆情形下才能够进行页面的访问
 * 作用在方法上，运行时有效
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}

