package com.nowcoder.community.controller;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/8 上午 08:17
 * @description
 */

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.LoginService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.CommunityUtil;
import com.nowcoder.community.utils.UserHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${community.domain}")
    private String domain;

    @Value("${community.uploadPath}")
    private String uploadPath;

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserHolder holder;

    //日志
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获得用户设置界面
     *
     * @return
     */
    @LoginRequired
    @GetMapping("/setting")
    public String getUserSettingPage() {
        return "/site/setting";
    }


    /**
     * 上传用户头像，根据上传结果响应到不同的页面进行显示
     * @param headerPic 头像图片
     * @param model  模型
     * @return
     */
    @PostMapping("/uploadHeader")
    public String uploadHeader(MultipartFile headerPic, Model model) {
        if (headerPic == null) {
            model.addAttribute("error", "请上传照片！");
            //回到设置页面
            return "/site/setting";
        }
        //校验上传的文件格式，是否为图片格式
        String fileName = headerPic.getOriginalFilename();
        //截获.之后的字符,文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        boolean flag = false;
        switch (suffix.toUpperCase()) {
            case "PNG":
                flag = true;
            case "JPEG":
                flag = true;
            case "JPG":
                flag = true; break;
            default:
                flag = false;
        }
        if (!flag) {
            model.addAttribute("error", "文件格式必须是图片格式，注意上传的文件必格式");
            //回到设置页面
            return "/site/setting";
        }

        //文件存放的路径
        String name= CommunityUtil.getUUID() + "." + suffix;
        String path = uploadPath + "/" + name;
        File file = new File(path);

        try {
            headerPic.transferTo(file);
        } catch (IOException e) {
            logger.error("上传文件错误", e);
            throw new RuntimeException("上传文件错误", e);
        }

        //更新用户头像
        //domain【localhost:8090/user/getUserHeaderPic/fileName】
        String headerUrl=domain+"/user/getUserHeaderPic/"+name;
        User user=holder.getUser();
        //数据库更新用户操作
        userService.updateUserHeader(user.getId(),headerUrl);
        //重定向到首页
        return "redirect:/index";
    }


    /**
     * 根据图片名获得图片信息
     * @param fileName 文件名
     * @param response
     */
    @GetMapping("/getUserHeaderPic/{filename}")
    public void getUserHeaderPic(@PathVariable("filename") String fileName, HttpServletResponse response){
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }

    }


    /**
     * 修改用戶密碼
     * @param oldPassport
     * @param newPassport
     * @param model
     * @param cookie 请求中的登录凭证
     * @return
     */
    @PostMapping("/updPassport")
    public String updateUserPassport(String oldPassport,String newPassport,Model model,@CookieValue("ticket") String cookie){
        //获得用户信息
        User user=holder.getUser();
        //验证原始密码是否正确
        String md5Str=CommunityUtil.getMd5Str(oldPassport+user.getSalt());
        if(!md5Str.equals(user.getPassword())){
            model.addAttribute("error1","密码输入错误");
            //验证密码错误时,停留在设置界面
            return "/site/setting";
        }else{
            //新旧密码不能重复
            if(oldPassport.equals(newPassport)){
                model.addAttribute("error2","密码设定不能与旧密码重复");
                //验证密码错误时,停留在设置界面
                return "/site/setting";
            }
            //生成新的密码并更新到数据库中
            String newStr=CommunityUtil.getMd5Str(newPassport+user.getSalt());
            userService.updateUserPassport(user.getId(),newStr);
            //修改登录凭证无效
            loginService.logOut(cookie);
            //跳转到登录界面 用户重新登录账号
            return "redirect:/login";
        }
    }





}
