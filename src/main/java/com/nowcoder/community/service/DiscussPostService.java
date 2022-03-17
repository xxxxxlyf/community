package com.nowcoder.community.service;

import com.nowcoder.community.entity.DiscussPost;

import java.util.List;

/**
 * @author 刘逸菲
 * @create 2022-03-15 18:40
 **/
public interface DiscussPostService {

    List<DiscussPost> getPosts(int userId, int offset, int limit);


    int getTotalRows(int userId);
}
