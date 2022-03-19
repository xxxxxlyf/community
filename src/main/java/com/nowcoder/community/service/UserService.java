package com.nowcoder.community.service;

import com.nowcoder.community.entity.User;

import java.util.Map;

public interface UserService {

    /**
     * 根据用户ID查询用户信息
     * @param id
     * @return
     */
    User selectUserById(int id);

    /**
     * 注册账号
     * @param user
     * @return
     */
    Map<String,Object> registerUser(User user);

    /***
     * 激活用户账号
     * @param userId 用户id
     * @param activationCode 激活码
     * @return
     */
    int activateUser(int  userId,String activationCode);
}
