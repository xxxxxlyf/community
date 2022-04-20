package com.nowcoder.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/20 下午 05:31
 * @description
 */
@Component
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 用户点赞【第一次点击点赞，再次点击取消点赞】
     * @param userId     用户id
     * @param entityId   点赞实体id
     * @param entityType 点赞实体类型id 1帖子 2评论
     *                   key为like:entity:entityType:entityId,存储的value为点赞的userid
     */
    public void like(int userId, int entityId, int entityType) {
        String key = "like:entity:" + entityType + ":" + entityId;

        //判断是否已点过赞
        Boolean member = redisTemplate.opsForSet().isMember(key, userId);
        if (member) {
            //点赞成功后，用户再次点击后取消该次点赞
            redisTemplate.opsForSet().remove(key, userId);
        }else{
            //添加点赞用户
            redisTemplate.opsForSet().add(key, userId);
        }
    }


    /**
     * 查询点赞数
     * @param entityId
     * @param entityType
     * @return
     */
    public long getEntityLikeQty(int entityId, int entityType){

        String key = "like:entity:" + entityType + ":" + entityId;
        return redisTemplate.opsForSet().size(key);
    }


    /**
     * 查询用户点赞状态 1点赞成功 0 未点赞
     * @param entityId
     * @param entityType
     * @return
     */
    public int getLikeStatus(int userId, int entityId, int entityType){
        String key = "like:entity:" + entityType + ":" + entityId;
        return redisTemplate.opsForSet().isMember(key, userId)?1:0;
    }

}
