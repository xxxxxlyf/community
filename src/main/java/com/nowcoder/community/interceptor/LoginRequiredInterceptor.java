package com.nowcoder.community.interceptor;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/8 下午 08:02
 * @description
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {


    @Autowired
    private UserHolder holder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断是否作用在方法上
        if(handler instanceof HandlerMethod){
            HandlerMethod   h=(HandlerMethod)handler;
            Method method = h.getMethod();
            //判断方法上是否标注了指定的注解
            LoginRequired annotation = method.getAnnotation(LoginRequired.class);
            if(annotation!=null&&holder.getUser()==null){
                //请求资源路径上判断为未登录状态，请求跳转到登录界面
                response.sendRedirect(request.getContextPath()+"/login");
                return false;
            }
        }else{
            //拦截的不是方法上是，不需要进行拦截器处理
            return true;
        }

        return true;

    }
}
