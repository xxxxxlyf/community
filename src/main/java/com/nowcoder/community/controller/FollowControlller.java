package com.nowcoder.community.controller;

import com.nowcoder.community.entity.CommunityConstant;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.service.impl.FollowService;
import com.nowcoder.community.utils.CommunityUtil;
import com.nowcoder.community.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


/**
 * @author lyf
 * @projectName community
 * @date 2022/4/23 下午 05:30
 * @description 用户关注Controller
 */
@Controller
public class FollowControlller {


    @Autowired
    private FollowService followService;


    @Autowired
    private UserHolder userHolder;


    @Autowired
    private UserService userService;



    @PostMapping("/follow")
    @ResponseBody
    public String follow(int entityType,int entityId){
        //获得当前操作用户
        User user=userHolder.getUser();
        followService.follow(user.getId(),entityType,entityId);
        return CommunityUtil.getJsonStr(0,"关注成功",null);
    }



    @PostMapping("/unfollow")
    @ResponseBody
    public String unfollow(int entityType,int entityId){
        //获得当前操作用户
        User user=userHolder.getUser();
        followService.unfollow(user.getId(),entityType,entityId);

        return CommunityUtil.getJsonStr(0,"取消关注成功",null);
    }



    @GetMapping("/getUserFollowees/{userId}")
    public String getUserFollowees(@PathVariable("userId") int userId, Model model, Page page){


        //查询当前用户
        User user = userService.selectUserById(userId);
        model.addAttribute("user",user);

        //设置分页信息
        page.setPath("/getUserFollowees/"+userId);
        page.setLimit(5);
        page.setRows((int) followService.getFolloweeQty(userId, CommunityConstant.ENTITY_TYPE_UER));
        List<Map<String, Object>> followInfoLists = followService.getFollowInfoLists(1, userId, page.getOffset(), page.getLimit());


        model.addAttribute("lists",followInfoLists);


        //回到页面处理页
        return "/site/followee";
    }



    @GetMapping("/getUserFollowers/{userId}")
    public String getUserFollowers(@PathVariable("userId") int userId, Model model, Page page){


        //查询当前用户
        User user = userService.selectUserById(userId);
        model.addAttribute("user",userId);

        //设置分页信息
        page.setPath("/getUserFollowers/"+userId);
        page.setLimit(5);
        page.setRows((int) followService.getFollowerQty(userId, CommunityConstant.ENTITY_TYPE_UER));
        List<Map<String, Object>> followInfoLists = followService.getFollowInfoLists(0, userId, page.getOffset(), page.getLimit());

        model.addAttribute("lists",followInfoLists);


        //回到页面处理页
        return "/site/followee";
    }
}
