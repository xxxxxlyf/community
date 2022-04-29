package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.CommunityConstant;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.CommunityUtil;
import com.nowcoder.community.utils.MailUtil;
import com.nowcoder.community.utils.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${community.domain}")
    private String domain;

    @Override
    public User selectUserById(int id) {
        User u=  getChache(id);
        if(u==null){
            u= mapper.selectUserById(id);
            //user 存入缓存
            addChache(u);
        }
        return u;
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
        //查询用户信息【激活用户后，调用内部方法将用户信息存入缓存】
        User user = selectUserById(userId);
        if (user != null) {
            if (user.getStatus() == 1) {
                return CommunityConstant.ACTIVATION_REPEAT;
            } else {
                if (user.getActivationCode().equals(activationCode)) {

                    mapper.updStatus(userId, 1);
                    return CommunityConstant.ACTIVATION_SUCCESS;
                } else {
                    return CommunityConstant.ACTIVATION_FAILED;
                }
            }
        } else {
            return CommunityConstant.ACTIVATION_REPEAT;
        }
    }

    @Override
    public int updateUserHeader(int userId,String headerUrl){

      //更新用户缓存
      int i=  mapper.updHeader(userId,headerUrl);
      if(i==1){
          //成功更新头像后删除用户缓存
          deleChache(userId);
      }
      return i;
    }

    @Override
    public int updateUserPassport(int userId, String password) {
        //更新用户缓存
        int i= mapper.updPassword(userId,password);
        if(i==1){
            //成功更新密码后删除用户缓存
            deleChache(userId);
        }
        return i;
    }


    //1 优先从redis缓存中获取用户
    private User getChache(int userId){
        String key= RedisKeyUtil.getUserKey(userId);
        User u= (User) redisTemplate.opsForValue().get(key);
        return u;
    }


    //2 数据更新后需要删除原来的缓存
    private void deleChache(int userId){
        String key= RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(key);
    }

    //3 插入用戶緩存
    private void addChache(User u){
        String key= RedisKeyUtil.getUserKey(u.getId());
        redisTemplate.opsForValue().set(key,u);
    }
}
