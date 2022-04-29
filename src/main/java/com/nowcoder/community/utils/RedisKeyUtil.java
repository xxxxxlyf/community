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

    //验证码前缀
    private static final String PREFIX_KAPTCHA="kaptcha";

    //登录凭证前缀
    private static  final String PREFIX_LOGIN_TICKET="loginTicket";


    private static final String PREFIX_USER="user";


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

    /**
     * key=kaptcha:owner
     * @param owner
     * @return
     */
    public static  String getKaptchaKey(String owner){
        return PREFIX_KAPTCHA+SPLIT_SYNTAX+owner;
    }


    /**
     * key=loginTicket:ticket String 类型，存的是LoginTicket
     * @param ticket
     * @return
     */
    public static  String getLoginTiketKey(String ticket){
        return PREFIX_LOGIN_TICKET+SPLIT_SYNTAX+ticket;
    }


    /**
     * key=user:userId String 类型
     * @param userId
     * @return
     */
    public static  String getUserKey(int userId){
        return PREFIX_USER+SPLIT_SYNTAX+userId;
    }
}
