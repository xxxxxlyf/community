package com.nowcoder.community.annotation;

/**
 * @author lyf
 * @description 登录注解
 * @create 2022-03-27 17:02
 **/

public @interface LoginRequired {

    /**
     * true时，标识当前不需要经过拦截器逻辑拦截
     * @return
     */
    boolean required() default false;
}
