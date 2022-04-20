package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.utils.CommunityUtil;
import com.nowcoder.community.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/20 下午 06:29
 * @description
 */
@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserHolder userHolder;


    /**
     * 用户点赞 异步请求，返回统一格式，ajax可以动态刷新页面
     * @param entityType
     * @param entityId
     * @return
     */
    @LoginRequired
    @GetMapping("/like")
    @ResponseBody
    public String userLike(int entityType,int entityId){
        int userId=userHolder.getUser().getId();
        likeService.like(userId,entityId,entityType);

        //like数
        long likeQty=likeService.getEntityLikeQty(entityId,entityType);
        //like status
        int likeStatus=likeService.getLikeStatus(userId,entityId,entityType);

        Map<String,Object>map=new HashMap<>();
        map.put("likeQty",likeQty);
        map.put("likeStatus",likeStatus);

        return CommunityUtil.getJsonStr(0,"success",map);
    }
}
