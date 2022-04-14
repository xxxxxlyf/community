package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.CommunityUtil;
import com.nowcoder.community.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author lyf
 * @description
 * @create 2022-04-13 22:09
 * 帖子
 **/
@Controller
@RequestMapping("/discussPost")
public class DiscussPostController {

    @Autowired
    private UserHolder holder;

    @Autowired
    private DiscussPostService postService;

    @Autowired
    private UserService userService;


    @ResponseBody
    @PostMapping("/addPost")
    public String addPost(String title,String content){
        User u=holder.getUser();
        if(postService==null){
            return CommunityUtil.getReturnMsg("您还没有登录哦！",403);
        }else{
            int i=  postService.addPosts(content,title,u.getId());
            if(i==1){
                return CommunityUtil.getReturnMsg("发布成功",0);
            }else {
                throw  new RuntimeException("数据执行异常，添加帖子失败");
            }
        }
    }


    /**
     * 查询帖子详情[根据帖子id]
     * @param id
     * @return
     */
    @LoginRequired
    @GetMapping("/getPostDetail/{id}")
    public String getPostDetail(@PathVariable("id") int id, Model model){
        DiscussPost post=postService.getPostById(id);
        model.addAttribute("post",post);
        //查詢作者信息
        User user=userService.selectUserById(post.getUserId());
        model.addAttribute("user",user);

        //返回帖子詳情的處理頁面
        return "/site/discuss-detail";
    }
}