package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/16 上午 10:18
 * @description
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService service;

    @Autowired
    private UserHolder holder;


    /**
     * 增加帖子评论
     * @param id
     * @param comment
     * @return
     */
    @PostMapping("/add/{id}")
    public String addComment(@PathVariable("id") int id, Comment comment){

        //获得当前评论的用户
        User user=holder.getUser();

        comment.setStatus(0);
        comment.setUserId(user.getId());
        comment.setCreateTime(new Date());
        service.addComment(comment);


        //重定向到帖子详情页面中
        return "redirect:/discussPost/getPostDetail/"+id;

    }
}
