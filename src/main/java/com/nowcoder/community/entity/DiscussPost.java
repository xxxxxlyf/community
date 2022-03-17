package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 刘逸菲
 * @create 2022-03-15 17:52
 * 帖子信息类
 **/
@Data
public class DiscussPost {

    private int id;
    private int  userId;
    private String title;
    private String content;
    private int type;
    private int status;
    private int commentCount;
    private double score;
    private Date createTime;

}
