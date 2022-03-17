package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 刘逸菲
 * @create 2022-03-15 18:45
 **/

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;

    @Override
    public User selectUserById(int id) {
        return mapper.selectUserById(id);
    }
}
