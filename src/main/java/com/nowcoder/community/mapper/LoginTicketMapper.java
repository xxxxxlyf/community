package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @author 刘逸菲
 * @create 2022-03-22 21:30
 * 登录凭证Mapper[采用注解形式编写]
 * Deprecated 标识该组件不推荐使用
 **/
@Mapper
@Repository
@Deprecated
public interface LoginTicketMapper {

    /**
     * 新增登录凭证信息
     * @param ticket
     * @return
     */
    @Insert({
            "insert login_ticket(user_id,ticket,status,expired)",
            "values (#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int addLoginTicket(LoginTicket ticket);

    /**
     * 修改登录凭证状态
     * @param ticket
     * @param status
     * @return
     */
    @Update({
            "<script>",
            "update login_ticket set status=#{status} ",
            "<if test=\"status==1\">",
            ",expired=now()",
            "</if>",
            "where ticket=#{ticket}",
            "</script>"

    })
    int updStatus(String ticket,int status);

    /**
     * 根据唯一凭证信息获得登录信息
     * @param ticket
     * @return
     */
    @Select("select * from login_ticket where ticket=#{ticket}")
    LoginTicket getTicketInfo(String ticket);
}
