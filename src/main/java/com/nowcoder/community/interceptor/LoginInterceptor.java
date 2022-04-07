package com.nowcoder.community.interceptor;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.LoginTicketMapper;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.utils.CookieUtil;
import com.nowcoder.community.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author lyf
 * @create 2022-03-23 22:35
 * @Description 登录拦截器
 **/
@Component
public class LoginInterceptor implements HandlerInterceptor {


    @Autowired
    private UserMapper mapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;


    @Autowired
    private UserHolder holder;


    /**
     * 进入Controller请求前执行
     * @param request
     * @param response
     * @param handler 被拦截的方法
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获得request中的cookie信息，是否携带了cookie信息
        String ticket= CookieUtil.getCookieValue(request,"ticket");
        if(ticket!=null){
            //根据ticket查询登录凭证
            LoginTicket loginTicket=loginTicketMapper.getTicketInfo(ticket);
            if(loginTicket!=null&&loginTicket.getStatus()==0&&loginTicket.getExpired().after(new Date())){
                //有效时段内，在进入Controller之前查询用户信息
                //在请求结束前，使用的是同一个线程来处理本次请求，存入到本地线程组中
                User user=mapper.selectUserById(loginTicket.getUserId());
                holder.setUser(user);
            }
        }
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
        //从线程中获得User对象
        User user=holder.getUser();
        if(user!=null&&modelAndView!=null){
            //向模型中写入待视图渲染的属性 user
            modelAndView.addObject("loginUser",user);
        }
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
        //移除本次请求中存放的数据，避免垃圾数据的堆积
        holder.remove();
    }
}
