package com.nowcoder.community;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommunityApplicationTests {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private DiscussPostMapper mapper1;

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

}
