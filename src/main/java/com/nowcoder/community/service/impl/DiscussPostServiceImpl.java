package com.nowcoder.community.service.impl;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 刘逸菲
 * @create 2022-03-15 18:40
 **/
@Service
public class DiscussPostServiceImpl implements DiscussPostService {

    @Autowired
    private DiscussPostMapper mapper;


    @Override
    public  List<DiscussPost> getPosts(int userId, int offset, int limit){
        return mapper.getPosts(userId, offset, limit);
    }

    @Override
    public int getTotalRows(int userId) {
        return mapper.countPost(userId);
    }

}
