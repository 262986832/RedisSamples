package com.vergilyn.demo.redis.listener.redis;

import com.vergilyn.demo.redis.config.JedisConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/8/3
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=RedisExpiredApplication.class)
public class RedisExpiredApplicationTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Before
    public void before() throws Exception {
        redisTemplate.execute((RedisConnection c) -> {
                c.flushAll();
                return null;
        });

        System.out.println(redisTemplate.opsForValue().get("kky"));
        redisTemplate.opsForValue().set(JedisConfig.DEFAULE_KEY, "123321");
    }

    @Test
    public void testExpiredListener(){
        System.out.println(redisTemplate.opsForValue().get("kky"));
    }
}