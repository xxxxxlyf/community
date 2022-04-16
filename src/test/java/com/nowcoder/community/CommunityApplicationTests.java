package com.nowcoder.community;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.CommentMapper;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.utils.MailUtil;
import com.nowcoder.community.utils.SensitivaWordsFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.transform.Source;
import java.util.Date;


@SpringBootTest
@RunWith(SpringRunner.class)
public class CommunityApplicationTests {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private DiscussPostMapper mapper1;

    @Autowired
    private CommentMapper mapper2;

    @Autowired
    private MailUtil util;

    @Autowired
    private SensitivaWordsFilter filter;

    @Test
    public  void contextLoads() {
        //测试插入对象
        User u=new User();
        u.setUsername("lyfxxp");
        u.setPassword("test12345");
        u.setEmail("1779178166@qq.com");
        u.setType(0);
        u.setStatus(0);
        u.setActivationCode("test");

        int id =mapper.addUser(u);
        System.out.println(id);
        //输出ID信息
        System.out.println(u.getId());
    }



    @Test
    public void test(){
        int i = mapper1.countPost(1);
        System.out.println(i);
    }

    @Test
    public void testMail(){

        util.sendMailMessage("1779178166@qq.com","test","test");
    }

    @Test
    public void testMd5(){
        //filter.init();
        String string = filter.filterWord("hello淫秽视频cXX");
        System.out.println(string);
    }

    @Test
    public void testMapper(){
        Comment comment=new Comment();
        comment.setStatus(0);
        comment.setUserId(10);
        comment.setCreateTime(new Date());
        mapper2.addComment(comment);

    }


}
