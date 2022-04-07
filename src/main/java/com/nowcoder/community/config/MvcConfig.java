package com.nowcoder.community.config;

import com.nowcoder.community.interceptor.LoginInterceptor;
import com.nowcoder.community.interceptor.LoginRequiresIntecptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lyf
 * @description mvc配置
 * @create 2022-03-23 22:45
 **/
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor interceptor;

    /**
     * 注册自定义的拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(interceptor)
                //在默认拦截所有路径下，不拦截以下静态资源
                .excludePathPatterns("/**/*.css","/**/*.js,","/**/*.png","/**/*.jpg","/**/*.html");

    }
}
