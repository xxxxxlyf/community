package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.impl.FollowService;
import com.nowcoder.community.utils.CommunityUtil;
import com.nowcoder.community.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


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

}
