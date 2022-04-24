package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.Message;
import com.nowcoder.community.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/16 下午 03:49
 * @description
 */
@Service
public class MessageService  {

    @Autowired
    private MessageMapper mapper;


    /**
     * 分页用户的查询消息列表【针对每个会话，只返回一条最新的私信】
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> getConversationList(int userId, int offset, int limit){
        return mapper.getConversationList(userId, offset, limit);
    }

    /**
     * 查询所有会话数目【用于分页】
     * @param userId
     * @return
     */
    public int getConversationCount(int userId){
        return mapper.getConversationCount(userId);
    }


    /**
     * 查询会话的所有私信列表
     * @param cId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> getLetters(String cId,int offset,int limit){

        return mapper.getLetters(cId, offset, limit);
    }

    /**
     * 根据会话id查询当前会话包含的所有私信数目【用于支持分页】
     * @param cId
     * @return
     */
    public int getLettersCount(String cId){
        return mapper.getLettersCount(cId);
    }


    /**
     * 查询当前用户未读的会话[未读私信数]
     * @param userId 用户id
     * @param cId 私信id
     * @return
     */
    public int getUnreadConversationCount(int userId,String cId){
        return mapper.getUnreadConversationCount(userId, cId);
    }
}
