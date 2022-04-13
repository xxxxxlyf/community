package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DiscussPostMapper {

    /**
     * 查询帖子信息
     * @param userId 用户id，查询条件，0时查所有帖子
     * @param offset
     * @param limit
     * @return
     */
    List<DiscussPost> getPosts(int userId,int offset,int limit);

    /**
     * 查询帖子总数【用于分页】
     * @param userId 用户id，查询条件，不传时查所有帖子
     * @return
     */
    int countPost(@Param("userId") int userId);

    /**
     * 增加一个用户帖子信息
     * @param content
     * @param title
     * @param userId
     * @return
     */
    int addPost(String content,String title,int userId);
}
