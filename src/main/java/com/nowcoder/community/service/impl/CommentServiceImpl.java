package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.CommunityConstant;
import com.nowcoder.community.mapper.CommentMapper;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.utils.SensitivaWordsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;


import java.util.List;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/15 上午 11:34
 * @description
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DiscussPostMapper postMapper;

    @Autowired
    private SensitivaWordsFilter filter;


    @Override
    public List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentByEntity(entityType, entityId, offset, limit);
    }

    @Override
    public int getCCountByEntity(int entityType, int entityId) {
        return commentMapper.getCCountByEntity(entityType, entityId);
    }

    /**
     * 声明式事务
     * 隔离级别：读已提交
     * 事务传播机制： A-B A存在时，B和A使用同一个事务。
     * 捕获异常时，将回滚数据
     * @param comment
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    @Override
    public int addComment(Comment comment) {
        if(comment==null){
            throw  new IllegalArgumentException("评论信息不能为空");
        }
        //过滤评论中的敏感词
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(filter.filterWord(comment.getContent()));
        //增加评论
        int i =commentMapper.addComment(comment);

        //仅在对帖子进行评论时，才需要更新帖子中的评论数
        if(comment.getEntityType()== CommunityConstant.COMMENT_TYPE_POST){

            //增加评论数
            postMapper.increaseCount(comment.getEntityId());
        }

        return i;
    }


}
