package com.nowcoder.community.service;

import com.nowcoder.community.entity.User;

public interface UserService {

    /**
     * 根据用户ID查询用户信息
     * @param id
     * @return
     */
    User selectUserById(int id);
}
