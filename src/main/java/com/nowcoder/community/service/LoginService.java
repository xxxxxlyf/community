package com.nowcoder.community.service;

import java.util.Map;

public interface LoginService {

    /**
     * 用户登录
     * @param username
     * @param password
     * @param expiredMs
     * @return
     */
    Map<String,String> login(String username,String password,long expiredMs);

    /**
     * 用户退出登录
     * @param ticket
     */
    void logOut(String ticket);

//    /**
//     * 忘记密码
//     * @return
//     */
//    Map<String,String> forgetPassword();
}
