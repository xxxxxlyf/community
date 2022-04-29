package com.nowcoder.community;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.utils.RedisKeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;


@SpringBootTest
@RunWith(SpringRunner.class)
public class CommunityApplicationTests {


    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void testOpsString(){
        String key="test:string";
        redisTemplate.opsForValue().set(key,"lyf");
        System.out.println(redisTemplate.opsForValue().get(key));

        System.out.println( redisTemplate.opsForValue().get("loginTicket:536c6dd926ee44ee9d6b7dde0cf3641f"));
        LoginTicket ticket=(LoginTicket) redisTemplate.opsForValue().get("loginTicket:536c6dd926ee44ee9d6b7dde0cf3641f");
        System.out.println(ticket);
    }

    @Test
    public void testOpsSet(){
        String key="test:set";
        //编程式事务,开启事务
        redisTemplate.multi();

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.opsForSet().add(key,1,2,3,4);
                //System.out.println(redisOperations.opsForSet().isMember(9));
                return null;
            }
        });

        redisTemplate.exec();

    }



    @Test
    public void testOpsZset(){
        String folleweeKey= RedisKeyUtil.getUsersFolloweeKey(151,3);
        Set range = redisTemplate.opsForZSet().range(folleweeKey, 0, -1);
        boolean contains = range.contains(111);
        System.out.println(contains);
    }
}
