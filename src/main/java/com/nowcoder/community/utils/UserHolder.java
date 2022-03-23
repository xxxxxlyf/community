package com.nowcoder.community.utils;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author lyf
 * @description 用户信息暂时存放处
 * @create 2022-03-23 23:27
 **/
@Component
public class UserHolder
{
    private ThreadLocal<User> local=new ThreadLocal<>();

    public void setUser(User user){
        local.set(user);
    }

    public User getUser(){
        return local.get();
    }


    public void remove(){
        local.remove();
    }

}
