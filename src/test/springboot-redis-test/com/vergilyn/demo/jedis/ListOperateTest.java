package com.vergilyn.demo.jedis;

import com.vergilyn.demo.redis.RedisApplication;

import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/4/4
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=RedisApplication.class)
public class ListOperateTest {
    private final static String KEY = "redis_key_list";
    // 2分钟内,最多投多少票
    private final static int MAX_VOTE = 20;
    // 刷票时间  2分钟
    private final static int MAX_TIME = 2 * 60; 
    
    @Autowired
    private Jedis jedis;

    @Before
    public void before(){
        // 清空数据
        System.out.println("before test >>>> clear: "+jedis.flushDB());
        // 准备数据
        initList();
    }


    @Test
    public void singleBaseTest(){
       List<String> list = null;
       
       
    }

    /**
     * 初始化测试数据:
     * <br>0-60s内投了5票; 60-90s投了3票
     * <br>value: 表示投票的时间
     */
    private void initList(){
        // 0-60s内投了5票, 
        jedis.lpush(KEY,"10");
        jedis.lpush(KEY,"20");
        jedis.lpush(KEY,"25");
        jedis.lpush(KEY,"45");
        jedis.lpush(KEY,"52");
        // 60-90s投了3票
        jedis.lpush(KEY,"65");
        jedis.lpush(KEY,"72");
        jedis.lpush(KEY,"80");
        
    }
}
