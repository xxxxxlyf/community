package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/16 下午 02:36
 * @description
 */
@Controller
@RequestMapping("/message")
public class MessageController {


    @Autowired
    private MessageService messageService;


    @Autowired
    private UserHolder holder;

    @Autowired
    private UserService userService;

    /**
     * 查询消息页面
     * LoginRequired 标注为必须要登录才能访问
     *
     * @return
     */
    @LoginRequired
    @GetMapping("/getMsgList")
    public String getPersonalMsg(Model model, Page page) {

        //查询用户
        User user = holder.getUser();
        //设置分页信息
        page.setPath("/message/getMsgList");
        page.setRows(messageService.getConversationCount(user.getId()));
        page.setLimit(5);

        //查询会话列表
        List<Message> conversationList = messageService.getConversationList(user.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Message conversation : conversationList) {
            Map<String, Object> conversationMap = new HashMap<>();
            //存入会话信息
            conversationMap.put("conversation",conversation);
            //查询会话对象
            int contactUserId= conversation.getFromId()==user.getId()?conversation.getToId():conversation.getFromId();
            User contactUser=userService.selectUserById(contactUserId);
            conversationMap.put("contactUser",contactUser);

            //查询会话中未读的消息数
            int unreadLetterQty=messageService.getUnreadConversationCount(user.getId(),conversation.getConversationId());
            conversationMap.put("unreadLetterQty",unreadLetterQty);

            //查询会话中总的消息数
            int letterQty=messageService.getLettersCount(conversation.getConversationId());
            conversationMap.put("letterQty",letterQty);


            //加入Map中
            maps.add(conversationMap);
        }


        //查询未读的会话数
        int unreadConversationQty=messageService.getUnreadConversationCount(user.getId(),null);
        model.addAttribute("unreadConversationQty",unreadConversationQty);
        //模型绑定待处理的数据
        model.addAttribute("map",maps);
        return "/site/letter";
    }


    /**
     * 查询会话详情
     * @param model 绑定数据model
     * @param cid 会话id
     * @return
     */
    @GetMapping("/letters/{cid}")
    public String getLetterDetail(@PathVariable("cid")String cid,Model model,Page page){
        //设置分页信息
        page.setPath("/message/letters/"+cid);
        page.setLimit(5);
        page.setRows(messageService.getLettersCount(cid));
        //查询会话对象
        int contactId=Integer.valueOf(cid.substring(0,cid.indexOf("_")))==holder.getUser().getId()?Integer.valueOf(cid.substring(cid.indexOf("_")+1)):Integer.valueOf(cid.substring(0,cid.indexOf("_")));
        User contactUser=userService.selectUserById(contactId);
        model.addAttribute("contactUser",contactUser);
        model.addAttribute("user",holder.getUser());
        //根据会话查询会话列表
        List<Message>lettersInfo=messageService.getLetters(cid,page.getOffset(),page.getLimit());
        List<Map<String,Object>>maps=new ArrayList<>();
        model.addAttribute("lettersInfo",lettersInfo);

        //返回视图页
        return "/site/letter-detail";
    }



}
