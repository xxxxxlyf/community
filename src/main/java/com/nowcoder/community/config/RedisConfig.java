package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author lyf
 * @projectName community
 * @date 2022/4/20 上午 11:35
 * @description 自定义redisTemplate
 */
@Configuration
public class RedisConfig {


    /**
     * 生成该Bean需要依赖RedisConnectionFactory 连接工厂
     * @param redisConnectionFactory
     * 替换默认的<Object,Object>的key-value存储数据的格式，采用<String,Object>的key-value存储数据格式
     * @return
     */
    @Bean
    public RedisTemplate<String,Object>redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object>redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //根据存储的键值数据类型，设置序列化的方式
        //设置key的序列化方式--字符串
        redisTemplate.setKeySerializer(RedisSerializer.string());
        //设置String-Value的序列化方式--Json
        redisTemplate.setValueSerializer(RedisSerializer.json());


        //设置Hash-key的序列化方式--字符串
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        //设置hash-Value的序列化方式--Json
        redisTemplate.setHashValueSerializer(RedisSerializer.json());


        //属性设置完后，生效属性
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
