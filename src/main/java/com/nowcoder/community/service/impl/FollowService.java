package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.CommunityConstant;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/23 下午 05:18
 * @description
 * key=followee:userId:entityType ZSet(EntityId,time)
 * key=follower:entityId:entityType ZSet(UserId,time)
 */
@Service
public class FollowService {


    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private UserService userService;


    /**
     * 关注
     * @param userId
     * @param entityType
     * @param entityId
     */
    public void follow(int userId,int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String folleweeKey= RedisKeyUtil.getUsersFolloweeKey(userId,entityType);
                String folleweerKey=RedisKeyUtil.getEntityFollwerKey(entityId,entityType);
                redisOperations.multi();

                redisOperations.opsForZSet().add(folleweeKey,entityId,System.currentTimeMillis());
                redisOperations.opsForZSet().add(folleweerKey,userId,System.currentTimeMillis());

                return redisOperations.exec();
            }
        });

    }


    /**
     * 取关
     * @param userId
     * @param entityType
     * @param entityId
     */
    public void unfollow(int userId,int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String folleweeKey= RedisKeyUtil.getUsersFolloweeKey(userId,entityType);
                String folleweerKey=RedisKeyUtil.getEntityFollwerKey(entityId,entityType);
                redisOperations.multi();

                redisOperations.opsForZSet().remove(folleweeKey,entityId);
                redisOperations.opsForZSet().remove(folleweerKey,userId);

                return redisOperations.exec();
            }
        });
    }


    /**
     * 查询用户关注某实体的数量
     * @param userId
     * @param entityType
     * @return
     */
    public long getFolloweeQty(int userId,int entityType){
        String folleweeKey= RedisKeyUtil.getUsersFolloweeKey(userId,entityType);
        return  redisTemplate.opsForZSet().size(folleweeKey);
    }


    /**
     * 查询某个实体的粉丝数
     * @param entityType
     * @param entityId
     * @return
     */
    public long getFollowerQty(int entityType,int entityId){
        String folleweerKey=RedisKeyUtil.getEntityFollwerKey(entityId,entityType);
        return  redisTemplate.opsForZSet().size(folleweerKey);

    }


    /**
     * 判断用户是否关注某实体
     * @param userId 用户id
     * @param entityType 实体类型
     * @param entityId 实体id
     * @return
     */
    public boolean hasFollowEntity(int userId,int entityType,int entityId){
        String folleweeKey= RedisKeyUtil.getUsersFolloweeKey(userId,entityType);
        Set range = redisTemplate.opsForZSet().range(folleweeKey, 0, -1);
        boolean contains = range.contains(entityId);
        return contains;
    }


    /**
     * 分页查询当前用户关注对象列表/用户粉丝列表
     * @param followType 1关注数 0 粉丝数
     * @param userId 查询的用户
     * @param offset 分页起始索引
     * @param limit 每页有效的行数
     * @return
     */
    public List<Map<String,Object>> getFollowInfoLists(int followType,int userId,int offset,int limit)
    {
        String key=followType==1? RedisKeyUtil.getUsersFolloweeKey(userId, CommunityConstant.ENTITY_TYPE_UER):RedisKeyUtil.getEntityFollwerKey(userId,CommunityConstant.ENTITY_TYPE_UER);
        //倒序查询，按照时间从大到小排
        Set<Integer> userIds = redisTemplate.opsForZSet().reverseRange(key, offset, offset + limit - 1);

        if(userIds==null){
            return null;
        }else{
            //根据用户id遍历信息
            List<Map<String,Object>>lists=new ArrayList<>();
            for(int id:userIds){
                Map<String,Object>map=new HashMap<>();
                //根据id查询分数
                User user = userService.selectUserById(id);
                map.put("user",user);

                //查询ZSET有序集合中的分数--时间
                Double score = redisTemplate.opsForZSet().score(key, id);
                Date followTime=new Date(score.longValue());
                map.put("followTime",followTime);
                lists.add(map);

                //查询当前用户是否关注列表中的对象
                boolean status=hasFollowEntity(userId,CommunityConstant.ENTITY_TYPE_UER,id);
                map.put("followStatus",status);
            }

            return lists;
        }
    }


}
