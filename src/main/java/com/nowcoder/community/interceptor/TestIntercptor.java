package com.nowcoder.community.interceptor;

import com.nowcoder.community.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/7 下午 02:48
 * @description
 */
@Component
public class TestIntercptor implements HandlerInterceptor {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("進入controller前");
        return true;
    }

    /**
     * 进入请求，在执行模板引擎之前执行
     * @param request
     * @param response
     * @param handler 被拦截的方法
     * @param modelAndView 模型与视图 肯能为空
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("返回結果前");
    }


    /**
     * 在模板引擎执行后，请求结束之前执行
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("after");
    }
}
