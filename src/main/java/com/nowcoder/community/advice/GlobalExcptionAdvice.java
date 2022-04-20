package com.nowcoder.community.advice;

import com.nowcoder.community.utils.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/18 下午 04:33
 * @description 全局异常处理
 */
@ControllerAdvice(annotations = {Controller.class})
public class GlobalExcptionAdvice {


    private Logger logger= LoggerFactory.getLogger(this.getClass());


    /**
     * 捕获异常，对异常做出处理
     * @param e
     * @param request
     * @param response
     */
    @ExceptionHandler({Exception.class})
    public void handException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("服务器异常"+e.toString());
        //遍历异常堆栈信息
        for(StackTraceElement element:e.getStackTrace()){
            logger.error(element.toString());
        }

        //根据请求方式来决定是返回自定义error页面还返回统一数据
        String requestHeader=request.getHeader("x-requested-with");
        if(("XMLHttpRequest").equals(requestHeader)){
             //异步调用，返回json数据
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getReturnMsg("服务器异常",1));
        }else{
            //普通页面请求
            //重定向到异常页面
            response.sendRedirect(request.getContextPath()+"/getError");
        }
    }

}
