package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.Message;

import java.util.List;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/16 下午 02:39
 * @description
 */
public interface MessageMapper {


    /**
     * 分页用户的查询消息列表【针对每个会话，只返回一条最新的私信】
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> getConversationList(int userId,int offset,int limit);

    /**
     * 查询所有会话数目【用于分页】
     * @param userId
     * @return
     */
    int getConversationCount(int userId);


    /**
     * 查询会话的所有私信列表
     * @param cId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> getLetters(String cId,int offset,int limit);

    /**
     * 根据会话id查询当前会话包含的所有私信数目【用于支持分页】
     * @param cId
     * @return
     */
    int getLettersCount(String cId);


    /**
     * 查询当前用户未读的会话[未读私信数]
     * @param userId 用户id
     * @param cId 私信id
     * @return
     */
    int getUnreadConversationCount(int userId,String cId);


}
