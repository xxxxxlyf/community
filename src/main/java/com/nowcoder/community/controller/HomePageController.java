package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 刘逸菲
 * @create 2022-03-15 18:51
 * 首页controller
 **/
@Controller
public class HomePageController {


    @Autowired
    private DiscussPostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    /**
     * 进入到首页【默认显示前10条帖子内容】
     * model springMvc 中的Model层，用于绑定vivew中处理的数据
     * @param model
     * @return
     */
    @GetMapping("/index")
    private String getIndex(Model model, Page page){
        //spring MVC 在处理前台传入的参数时，会自动注入并将参数信息绑定到MODEL上,所以不需要手动的绑定参数到Model上
        int rows=postService.getTotalRows(0);
        page.setRows(rows);
        page.setPath("/index");
        List<Map<String,Object>>postsInfo=new ArrayList<>();
        //查询前10条帖子信息
        List<DiscussPost> posts=postService.getPosts(0,page.getOffset(),page.getLimit());
        if(posts!=null&posts.size()>0){
            //遍历帖子信息，查询帖子用户信息
            for (DiscussPost post : posts) {
                Map<String,Object>map=new HashMap<>();
                map.put("post",post);

                User user=userService.selectUserById(post.getUserId());
                map.put("user",user);

                long likeQty=likeService.getEntityLikeQty(post.getId(),1);
                map.put("likeQty",likeQty);

                //数据写入
                postsInfo.add(map);
            }
        }

        //模型绑定需要处理的数据
        model.addAttribute("discussPosts",postsInfo);
        //返回视图 templates/index.html,thymeleaf
        return "/index";
    }


    /**
     *
     * 返回服務器500异常页面
     * @return
     */
    @GetMapping("/getError")
    public String getError(){
        return "/error/500";

    }


}
