package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 刘逸菲
 * @create 2022-03-15 16:22
 **/
@Mapper
@Repository
public interface UserMapper {

    /**
     * 根据用户ID查询用户信息
     * @param id
     * @return
     */
    User selectUserById(int id);

    /**
     * 根据用户名称查询用户
     * @param username
     * @return
     */
    User selectUserByUsername( String username);

    /**
     * 根据邮箱查询用户信息
     * @param email
     * @return
     */
    User selectUserByEmail( String email);

    /**
     * 新增用户
     * @param user
     * @return
     */
    int addUser(User user);

    /**
     * 修改用户status
     * @param id
     * @param status
     * @return
     */
    int updStatus( int id, int status);

    /**
     * 修改用户密码
     * @param id
     * @param password
     * @return
     */
    int updPassword(int id, int password);

    /**
     * 修改用户头像
     * @param id
     * @param headerUrl
     * @return
     */
    int updHeader(int id,String headerUrl);


}
