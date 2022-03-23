package com.nowcoder.community.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lyf
 * @description cookie工具类
 * @create 2022-03-23 23:04
 **/
public class CookieUtil {


    /**
     * 根据名字获得存入cooKie中的值
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest request,String cookieName){
       //拿到所有的cookie数组
        Cookie[] cookies = request.getCookies();
        if(cookies!=null|| StringUtils.isBlank(cookieName)){
            throw  new IllegalArgumentException("参数不为空");
        }else{

            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(cookieName)){
                    return cookie.getValue();
                }
            }

            return null;
        }

    }
}
