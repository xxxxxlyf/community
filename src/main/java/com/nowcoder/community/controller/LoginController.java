package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.CommunityConstant;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.LoginService;
import com.nowcoder.community.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * @author 刘逸菲
 * @create 2022-03-18 23:23
 * 登录注册功能
 **/
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private Producer producer;

    @Autowired
    private LoginService loginService;

    /**
     * 返回注册界面视图
     * @return
     */
    @GetMapping("/getRegisterPage")
    public  String getRegisterPage(){
        //注册界面视图路径 classpath:templates/site/register.html
        return "/site/register";
    }

    /**
     * 返回登录界面视图
     * @return
     */
    @GetMapping("/login")
    public  String getLoginPage(){
        //注册界面视图路径 classpath:templates/site/register.html
        return "/site/login";
    }


    /**
     * 注册用户
     * @param model
     * @param user
     */
    @PostMapping("/registerUser")
    public String registerUser(Model model, User user){

         Map<String, Object> map = userService.registerUser(user);
         if(map==null||map.keySet().isEmpty()){
             //等待账户激活后续登录操作
             model.addAttribute("msg","账号注册成功，已经向您的邮箱发送了账号激活邮件，请尽快激活账号");
             model.addAttribute("target","/index");
             return "/site/operate-result";
         }else{
             model.addAttribute("user",user);
             model.addAttribute("usernameExp",map.get("usernameExp"));
             model.addAttribute("passwordExp",map.get("passwordExp"));
             model.addAttribute("emailExp",map.get("emailExp"));
             return "/site/register";

         }
    }


    /**
     * //http://localhost:8090/user/activation/userId/code
     * @param userId
     * @param activationCode
     * @return
     */
    @GetMapping(path = "/user/activation/{userId}/{activationCode}")
    public String activateUser(Model model,@PathVariable("userId") int userId,@PathVariable("activationCode")  String activationCode){
        int i = userService.activateUser(userId, activationCode);
        if(i== CommunityConstant.ACTIVATION_SUCCESS){
            model.addAttribute("msg","账号已成功激活，可登录使用");
            //登录返回登录界面
            model.addAttribute("target","/login");
        }else if(i== CommunityConstant.ACTIVATION_REPEAT){
            model.addAttribute("msg","账号无需重复激活");
            //返回首页
            model.addAttribute("target","/index");
        }else if(i== CommunityConstant.ACTIVATION_FAILED){
            model.addAttribute("msg","账号信息错误或激活码错误，激活账号失败");
            //返回首页
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }


    /**
     * 获得生成的随机验证码
     * @param response
     * @param httpSession
     */
    @GetMapping("/login/getKapatchaImage")
    public void getKapatchaImage(HttpServletResponse response, HttpSession httpSession){
        //生成验证码
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);

        //验证码字符串写入到session中.服务器将依托cookie 【key,value】value中存放的是当前生成的sessionid.
        //以后浏览器访问浏览器时，将会携带该cookie信息，使得请求信息是有状态的
        httpSession.setAttribute("kapatchaStr", text);

        //图片响应给response
        //响应格式为.png的图片
        response.setContentType("image/png");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(image,"png",outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    /**
     * 用户登录账号 post方式
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param rememberMe 是否记住密码
     * @Param model 用于绑定视图中的数据
     * @Param httpSession session【储存在服务端】
     * @return
     */
    @PostMapping("/loginUser")
    public String loginUser(String username, String password, String code, boolean rememberMe, Model model,HttpSession session,HttpServletResponse response){
        //验证码是否正确
        //从session中获得之前存放的验证码【验证码比较时忽略大小写】
        String o = (String)session.getAttribute("kapatchaStr");
        if(StringUtils.isBlank(o)||StringUtils.isBlank(code)||!code.toUpperCase().equals(o)){
            model.addAttribute("codeError","验证码输入错误");
            //登录失败，返回登录页面
            return "/site/login";
        }

        int expiredMs=rememberMe?CommunityConstant.REMEMBER_EXPIRED_SECONDS:CommunityConstant.DEFAULT_EXPIRED_SECONDS;
        Map<String,String> map=loginService.login(username,password,expiredMs);
        if(map.containsKey("ticket")){
            //登录成功后，重定向到首页
            return "redirect:/index";
        }else{
            model.addAttribute("userError",map.get("userError"));
            model.addAttribute("passwordError",map.get("passwordError"));
            //cookie中存入登录用户凭证
            //cookie是一个特殊的键值对信息，存放重要的信息
            Cookie cookie=new Cookie("ticket",map.get("ticket"));
            //cookie设置最大有效时间，设置这个属性时，不会存在用户端的内存中，而是存放在客户端的硬盘空间中
            cookie.setMaxAge((int) expiredMs);
            //设置有效的路径
            cookie.setPath("/");
            //响应体中存入cookie，后续继续发送请求时将会携带该cookie，保证了请求访问的连续性
            response.addCookie(cookie);
            //返回登录首页
            return "/site/login";
        }
    }


    @GetMapping("/logout")
    public String logOut(@CookieValue("ticket") String cookieValue){
        loginService.logOut(cookieValue);
        //重定向到登录页面
        return "redirect:/login";
    }
}
