package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 刘逸菲
 * @create 2022-03-22 21:25
 * 登录凭证类
 **/
@Data
public class LoginTicket {

    /**
     * 行标
     */
    private int id;
    /**
     * 用户id
     */
    private int userId;
    /**
     * ticket凭证
     */
    private String ticket;
    /**
     * 用户状态
     */
    private int status;
    /**
     * 过时间
     */
    private Date expired;
}
