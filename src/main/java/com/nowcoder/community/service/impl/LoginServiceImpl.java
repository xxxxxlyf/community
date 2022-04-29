package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.LoginTicketMapper;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.service.LoginService;
import com.nowcoder.community.utils.CommunityUtil;
import com.nowcoder.community.utils.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘逸菲
 * @create 2022-03-22 22:11
 **/
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;
//    @Autowired
//    private LoginTicketMapper ticketMapper;

    @Override
    public Map<String, String> login(String username, String password, long expiredMs) {
         Map<String,String>map=new HashMap<>();
         //校验数据
         if(StringUtils.isBlank(username)){
             map.put("userError","用户名不为空");
             return map;
         }

         User user=userMapper.selectUserByUsername(username);
         if(user==null){
             map.put("userError","用户名错误");
             return map;
         }

         if(user.getStatus()==0){
             map.put("userError","账户未激活，登录失败");
             return map;
         }

         //校验密码
         String pss=CommunityUtil.getMd5Str(password+user.getSalt());
         if(!pss.equals(user.getPassword())){
             map.put("passwordError","密码错误，登录失败");
             return map;
         }
         //插入登录凭证
         LoginTicket ticket=new LoginTicket();
         ticket.setExpired(new Date(System.currentTimeMillis()+expiredMs*1000));
         ticket.setUserId(user.getId());
         ticket.setStatus(0);
         ticket.setTicket(CommunityUtil.getUUID());

         //hash 数据结构验证
         String key= RedisKeyUtil.getLoginTiketKey(ticket.getTicket());
         //存入loginTiket的相关信息,一个序列化的字符串,意即Hash结构
         redisTemplate.opsForValue().set(key,ticket);
         //ticketMapper.addLoginTicket(ticket);

         map.put("ticket",ticket.getTicket());

         return map;

    }

    @Override
    public void logOut(String ticket) {
        //修改登录凭证的状态，1表示退出登录
        //ticketMapper.updStatus(ticket,1);

        //删除redis中的loginTicket
        String key= RedisKeyUtil.getLoginTiketKey(ticket);
        LoginTicket loginTicket=(LoginTicket) redisTemplate.opsForValue().get(key);
        loginTicket.setStatus(1);

        redisTemplate.opsForValue().set(key,loginTicket);
    }

    @Override
    public LoginTicket getLoginTicket(String ticket) {
        String key= RedisKeyUtil.getLoginTiketKey(ticket);
        LoginTicket loginTicket=(LoginTicket) redisTemplate.opsForValue().get(key);
        return loginTicket;
    }


}
