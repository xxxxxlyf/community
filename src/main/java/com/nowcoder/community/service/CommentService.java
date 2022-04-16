package com.nowcoder.community.service;

import com.nowcoder.community.entity.Comment;

import java.util.List;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/15 上午 11:34
 * @description
 */
public interface CommentService {


    /**
     * 分页查询帖子的信息
     * @param entityType 评论对象类型
     * @param entityId 评论对象id
     * @param offset 偏移量
     * @param limit 每页的分页数目
     * @return
     */
    List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit);


    /**
     * 查询评论的总数，用于页面分页
     * @param entityType 评论对象类型
     * @param entityId 评论对象id
     * @return
     */
    int getCCountByEntity(int entityType,int entityId);


    /**
     * 插入评论
     * @param comment
     * @return
     */
    int addComment(Comment comment);



}
