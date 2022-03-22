package com.nowcoder.community.entity;

/**
 * @author 刘逸菲
 * @create 2022-03-19 14:45
 * 定义激活状态常量值
 **/
public class CommunityConstant {

    /**
     * 成功激活
     */
    public  static int ACTIVATION_SUCCESS=0;
    /**
     * 重复激活动作
     */
    public static int ACTIVATION_REPEAT=1;

    /**
     * 激活失败
     */
    public  static int ACTIVATION_FAILED=2;

    /**
     * 默认过时间为1day
     */
    public static int DEFAULT_EXPIRED_SECONDS=60*60*24;

    /**
     * 记住密码时候有效时间为30day
     */
    public static int REMEMBER_EXPIRED_SECONDS=60*60*24*30;
}
