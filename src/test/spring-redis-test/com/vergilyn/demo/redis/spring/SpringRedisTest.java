package com.vergilyn.demo.redis.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/4/2
 */
@ContextConfiguration(locations = {"classpath:spring-redis-application.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringRedisTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 测试插入与获取Redis的数据
     */
    @Test
    public void testPutAndGet() {
        redisTemplate.opsForHash().put("user", "name", "vergilyn");
        Object object = redisTemplate.opsForHash().get("user", "name");
        System.out.println(object);
    }

    /**
     * 测试Redis作为缓存的例子
     */
    @Test
    public void testCache() throws InterruptedException {
        // 插入一条数据
        redisTemplate.opsForHash().put("user", "name", "vergilyn");
        // 设置失效时间为2秒
        redisTemplate.expire("user", 2, TimeUnit.SECONDS);
        Thread.sleep(1000);
        // 1秒后获取
        Object object = redisTemplate.opsForHash().get("user", "name");
        System.out.println("1秒后，重新获取：" + object);
        redisTemplate.opsForHash().put("user", "name","dante");
        System.out.println("2秒前，重新获取：" + redisTemplate.opsForHash().get("user", "name"));
        Thread.sleep(1000);
        // 2秒后获取
        object = redisTemplate.opsForHash().get("user", "name");
        System.out.println("2秒后，重新获取：" + object);
    }
}
