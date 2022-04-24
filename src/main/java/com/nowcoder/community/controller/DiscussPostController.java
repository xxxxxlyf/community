package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.*;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.service.impl.LikeService;
import com.nowcoder.community.utils.CommunityUtil;
import com.nowcoder.community.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;


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
    public String getPostDetail(@PathVariable("id") int id, Model model, Page page){
        DiscussPost post=postService.getPostById(id);
        model.addAttribute("post",post);
        //查詢作者信息
        User user=userService.selectUserById(post.getUserId());
        model.addAttribute("user",user);

        //查询帖子的点赞数
        long postLikeQty=likeService.getEntityLikeQty(id,1);
        model.addAttribute("postLikeQty",postLikeQty);

        //设置分页信息
        page.setPath("/discussPost/getPostDetail"+id);
        //每页显示5条数据
        page.setLimit(5);
        //设置评论总数
        page.setRows(post.getCommentCount());
        List<Map<String,Object>>commentVOList=new ArrayList<>();
        //查询当前帖子的所有评论信息
        List<Comment>commentList=commentService.selectCommentByEntity(CommunityConstant.ENTITY_TYPE_POST,id,page.getOffset(),page.getLimit());
          //展现帖子评论信息同时还需要展现评论用户的信息
          //遍历评论信息查询评论用户信息
          if(commentList!=null){

              for (Comment comment :commentList){
                  Map<String,Object>map=new HashMap<>();
                  map.put("comment",comment);
                  map.put("commentUser",userService.selectUserById(comment.getUserId()));

                  //查询每个回复的赞的数目
                  long commentLikeQty=likeService.getEntityLikeQty(comment.getId(),2);
                  map.put("commentLikeQty",commentLikeQty);

                  //查询回复[不进行分页展示]
                  List<Comment>replyComments=commentService.selectCommentByEntity(CommunityConstant.ENTITY_TYPE_REPLY,comment.getId(),0,Integer.MAX_VALUE);
                  if(replyComments!=null){
                      //遍历回复评论展现信息
                      List<Map<String,Object>>replyCommentVOList=new ArrayList<>();
                      for (Comment replyComment :replyComments){
                          Map<String,Object>replyMap=new HashMap<>();
                          replyMap.put("replyComment",replyComment);
                          replyMap.put("replyUser",userService.selectUserById(replyComment.getUserId()));

                          User target=replyComment.getTargetId()==0?null:userService.selectUserById(replyComment.getTargetId());
                          replyMap.put("target",target);


                          //查询贴的点赞数
                          long replyCQty=likeService.getEntityLikeQty(replyComment.getId(),2);
                          replyMap.put("replyCQty",replyCQty);
                          //查询回复评论的总数
                          int replyCommentCount=commentService.getCCountByEntity(CommunityConstant.ENTITY_TYPE_REPLY,replyComment.getId());
                          System.out.println(replyComment.getId());
                          System.out.println(replyCommentCount);
                          replyMap.put("replyCommentCount",replyCommentCount);

                          replyCommentVOList.add(replyMap);

                      }
                      map.put("replyVOList",replyCommentVOList);

                  }

                  //查询回复帖子的总数
                  int replyPostCommentCount=commentService.getCCountByEntity(CommunityConstant.ENTITY_TYPE_REPLY,comment.getId());
                  map.put("replyPostCommentCount",replyPostCommentCount);
                  commentVOList.add(map);

              }


          }

           //模型绑定传递需要处理的评论数据
           model.addAttribute("commentVOList",commentVOList);


        //返回帖子詳情的處理頁面
        return "/site/discuss-detail";
    }

}