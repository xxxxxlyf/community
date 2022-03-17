package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 刘逸菲
 * @create 2022-03-15 13:09
 * 用户信息表
 **/
@Data
public class User {

    private int id;
    private String username;
    private String password;
    private String salt;
    private int type;
    private int status;
    private String email;
    private String activationCode;
    private String headerUrl;
    private Date createTime;

}
