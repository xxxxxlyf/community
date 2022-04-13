package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.utils.SensitivaWordsFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author lyf
 * @create 2022-03-15 18:40
 **/
@Service
public class DiscussPostServiceImpl implements DiscussPostService {

    @Autowired
    private DiscussPostMapper mapper;

    @Autowired
    private SensitivaWordsFilter filter;


    @Override
    public  List<DiscussPost> getPosts(int userId, int offset, int limit){
        return mapper.getPosts(userId, offset, limit);
    }

    @Override
    public int getTotalRows(int userId) {
        return mapper.countPost(userId);
    }

    @Override
    public int addPosts(String content, String title, int userid) {
        if(StringUtils.isBlank(content)||StringUtils.isBlank(title)){
            throw new IllegalArgumentException("标题与内容均不能为空");
        }
        //移除可能携带的javaScript tag
        content= HtmlUtils.htmlEscape(content);
        title=HtmlUtils.htmlEscape(title);
        //标题，内容中信息的敏感词过滤
        content=filter.filterWord(content);
        title=filter.filterWord(title);
        int i= mapper.addPost(content, title, userid);
        return i;
    }

}
