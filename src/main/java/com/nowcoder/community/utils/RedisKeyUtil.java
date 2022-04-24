package com.nowcoder.community.utils;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/22 下午 03:52
 * @description 生成redis-key的工具方法
 */
public class RedisKeyUtil {


    //分隔符
    private static  final String SPLIT_SYNTAX=":";

    //实体点赞前缀
    private static  final String PREFIX_ENTITY_LIKE="like:entity:";

    //用戶收到赞前缀
    private static  final  String PREFIX_USER_LIKE="like:user:";

    //用户关注的对象
    private static  final String PREFIX_FOLLOWEE="followee";

    //关注某个实体的粉丝信息
    private static  final String PREFIX_FOLLOWER="follower";


    /**
     * like:entity:entityType:entityId
     */
    public static String getEntityLikeKey(int entityType,int entityId){
        return PREFIX_ENTITY_LIKE+entityType+SPLIT_SYNTAX+entityId;
    }

    /**
     * 用戶赞key
     * like:entity:userId
     * @param userId
     * @return
     */
    public static String getUserLikeKey(int userId){
        return PREFIX_USER_LIKE+userId;
    }


    /**
     * key=followee:userId:entityType ZSet(EntityId,time)
     * @param userId 用户id
     * @param entityType 关注的实体类型
     * @return
     */
    public static String getUsersFolloweeKey(int userId,int entityType){
        return PREFIX_FOLLOWEE+SPLIT_SYNTAX+userId+SPLIT_SYNTAX+entityType;
    }


    /**
     * key=follower:entityId:entityType ZSet(UserId,time)
     * @param entityId 被关注对象的实体id
     * @param entityType 被关注对象的类型
     * @return
     */
    public static String getEntityFollwerKey(int entityId,int entityType){
        return PREFIX_FOLLOWER+SPLIT_SYNTAX+entityId+SPLIT_SYNTAX+entityType;
    }
}
