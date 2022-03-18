package com.nowcoder.community.utils;


import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author 刘逸菲
 * @create 2022-03-18 23:56
 *
 **/
public class CommunityUtil {

    /**
     * 获得UUID字符串
     * @return
     */
    public static  String getUUID(){
        //截取UUID中的所有-
        return UUID.randomUUID().toString().replace("-","");
    }

    /**
     * 对原始字符串进行MD5加密
     * MD5单向加密，不能进行解密【key一样时，加密后的值始终保持一致】
     * @param origin 原始字符串
     * @return
     */
    public static  String getMd5Str(String origin){
        if(StringUtils.isBlank(origin)){
            return "";
        }else{
            //生成MD5加密串，16进制格式
            return DigestUtils.md5DigestAsHex(origin.getBytes());
        }
    }
}
