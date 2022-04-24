package com.nowcoder.community.service.impl;

import com.nowcoder.community.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
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
     * @param entityUserId 被赞的实体id
     * key为like:entity:entityType:entityId,存储的value为点赞的userid
     * 2022/4/23 重构点赞方法  记录点赞用户信息【set数据结构】还需记录点赞数目信息【String】
     * redis多条指令的执行，需要用到redis事务，保证数据的一致性
     */
    public void like(int userId, int entityId, int entityType,int entityUserId) {
        //记录点赞对象的用户id
        String entity_like_key = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        //记录用户获赞的数目
        String user_like_key=RedisKeyUtil.getUserLikeKey(entityUserId);
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                //判断是否已点过赞
                Boolean member = redisOperations.opsForSet().isMember(entity_like_key, userId);
                //开启事务
                redisOperations.multi();
                if (member) {
                    //点赞成功后，用户再次点击后取消该次点赞
                    redisOperations.opsForSet().remove(entity_like_key, userId);
                    //减少被赞用户的点赞数
                    redisOperations.opsForValue().decrement(user_like_key);
                } else {
                    //添加点赞用户
                    redisOperations.opsForSet().add(entity_like_key, userId);
                    //增加点赞数
                    redisOperations.opsForValue().increment(user_like_key);
                }

                //multi指令后入队列的所有指令全部一起执行，必须强调与数据库事务不同的是，开启事务后，查询语句在同一个session中是能查询到的
                return  redisOperations.exec();
            }
        });
    }


    /**
     * 查询点赞数
     * @param entityId
     * @param entityType
     * @return
     */
    public long getEntityLikeQty(int entityId, int entityType){

        String key =  RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().size(key);
    }


    /**
     * 查询用户点赞状态 1点赞成功 0 未点赞
     * @param entityId
     * @param entityType
     * @return
     */
    public int getLikeStatus(int userId, int entityId, int entityType){
        String key =  RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().isMember(key, userId)?1:0;
    }



    /**
     * 查询某个用户获得点赞数
     * @param userId 用户id
     * @return
     */
    public int getUserLikeQty(int userId){

        String key =  RedisKeyUtil.getUserLikeKey(userId);
        Integer o = (Integer)redisTemplate.opsForValue().get(key);
        int qty=o==null?0:o.intValue();
        return qty;
    }

}
