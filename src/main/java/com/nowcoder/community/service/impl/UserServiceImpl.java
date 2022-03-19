package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.ActivationConstant;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.CommunityUtil;
import com.nowcoder.community.utils.MailUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author 刘逸菲
 * @create 2022-03-15 18:45
 **/

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.domain}")
    private String domain;

    @Override
    public User selectUserById(int id) {
        return mapper.selectUserById(id);
    }

    @Override
    public Map<String, Object> registerUser(User user) {
        Map<String, Object> map = new HashMap<>();
        //参数校验
        if (user == null) {
            throw new IllegalArgumentException("用户信息不能为空");
        }

        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameExp", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordExp", "密码不能为空");
            return map;
        }

        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailExp", "邮箱地址不能为空");
            return map;
        }


        //数据是否重复
        User u = mapper.selectUserByUsername(user.getUsername());
        if (u != null) {
            map.put("usernameExp", "用户名已被注册，不可重复！");
            return map;
        }
        u = mapper.selectUserByEmail(user.getEmail());
        if (u != null) {
            map.put("emailExp", "邮箱已被注册！");
            return map;
        }

        //插入用户信息
        //密码需要进行加密
        user.setSalt(CommunityUtil.getUUID().substring(0, 5));
        user.setPassword(CommunityUtil.getMd5Str(user.getPassword() + user.getSalt()));
        user.setType(0);//普通用户
        user.setStatus(0);//待激活
        user.setActivationCode(CommunityUtil.getUUID());//用户待激活码
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));//设置用户注册的默认头像
        mapper.addUser(user);

        //发送激活邮件
        Context context = new Context();
        context.setVariable("user", user.getUsername());
        //http://localhost:8090/user/activation/userId/code
        String activationUrl = domain + "/user/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", activationUrl);

        String s = templateEngine.process("/mail/activation", context);
        mailUtil.sendMailMessage(user.getEmail(), s, "账号激活");
        return map;
    }

    @Override
    public int activateUser(int userId, String activationCode) {
        //查询用户信息
        User user = mapper.selectUserById(userId);
        if (user != null) {
            if (user.getStatus() == 1) {
                return ActivationConstant.ACTIVATION_REPEAT;
            } else {
                if (user.getActivationCode().equals(activationCode)) {

                    mapper.updStatus(userId, 1);
                    return ActivationConstant.ACTIVATION_SUCCESS;
                } else {
                    return ActivationConstant.ACTIVATION_FAILED;
                }
            }
        } else {
            return ActivationConstant.ACTIVATION_REPEAT;
        }
    }
}
