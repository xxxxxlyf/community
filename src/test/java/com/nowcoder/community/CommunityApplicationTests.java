package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.junit4.SpringRunner;




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
}
