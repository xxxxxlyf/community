package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/15 上午 10:46
 * @description 评论实体类
 */
@Data
public class Comment {

    /**
     * 评论唯一id
     */
    private int id;
    /**
     * 用户id
     */
    private int userId;
    /**
     * 评论对象类型 1帖子，2评论
     */
    private int entityType;
    /**
     * 评论对象唯一id
     */
    private int entityId;
    /**
     *
     */
    private int targetId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论状态
     */
    private int status;
    /**
     * 发布评论时间
     */
    private Date createTime;
}
