package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/16 下午 02:34
 * @description 消息 实体类
 */
@Data
public class Message {


    private int id;
    private int fromId;
    private int toId;
    private String conversationId;
    private String content;
    private int status;
    private Date createTime;
}
