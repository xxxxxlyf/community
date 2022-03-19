package com.nowcoder.community.controller;

import com.nowcoder.community.entity.ActivationConstant;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
        if(i== ActivationConstant.ACTIVATION_SUCCESS){
            model.addAttribute("msg","账号已成功激活，可登录使用");
            //登录返回登录界面
            model.addAttribute("target","/login");
        }else if(i==ActivationConstant.ACTIVATION_REPEAT){
            model.addAttribute("msg","账号无需重复激活");
            //返回首页
            model.addAttribute("target","/index");
        }else if(i==ActivationConstant.ACTIVATION_FAILED){
            model.addAttribute("msg","账号信息错误或激活码错误，激活账号失败");
            //返回首页
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }
}
