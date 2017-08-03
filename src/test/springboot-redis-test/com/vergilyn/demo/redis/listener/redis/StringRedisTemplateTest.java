package com.vergilyn.demo.redis.listener.redis;

import com.vergilyn.demo.redis.RedisApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/4/2
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes=RedisApplication.class)
public class StringRedisTemplateTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void setRedisTemplate(){
        redisTemplate.opsForValue().set("name","vergilyn");
        System.out.println(redisTemplate.opsForValue().get("name"));
    }
}
