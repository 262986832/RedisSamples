package com.vergilyn.demo.jedis;

import com.vergilyn.demo.redis.RedisApplication;
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
public class StringOperateTest {
    @Autowired
    private Jedis jedis;

    @Before
    public void before(){
        // 清空数据
        System.out.println("before test >>>> clear: "+jedis.flushDB());
    }

    /**
     * jedis单一操作测试：增删改查。
     */
    @Test
    public void jedisSingleTest(){

        System.out.println("test database >>>> key='key001', value='value001'");

        System.out.println("add >>>> : " +  jedis.set("key001","value001"));
        System.out.println("get >>>> : " +  jedis.get("key001"));

        jedis.set("key001","value001-update");  //1、直接覆盖原来的数据
        System.out.println("update >>>> : " + jedis.get("key001"));

        jedis.append("key001",",append");  //2、原值的基础上append
        System.out.println("append >>>> : " + jedis.get("key001"));

        System.out.println("isExists >>>> : " + jedis.exists("key001"));

        System.out.println("del >>>> : " + jedis.del("key001"));
        System.out.println("del get >>>> : " + jedis.get("key001"));
    }

    /**
     * jedis批量操作测试：增删改查。
     */
    @Test
    public void jedisMultiTest(){
        /**
         * 不存在的key并不会抛异常。如下：key004
         */
        String mset = jedis.mset("key001", "value001", "key002", "value002", "key003", "value003");
        System.out.println("multi-set ：" + mset);
        System.out.println("multi-exists : " + jedis.exists("key001","key002","key004"));
        System.out.println("multi-get ：" + jedis.mget("key001","key003","key004"));
        System.out.println("multi-del : " + jedis.del("key001","key003","key004")); // 返回2,不存在的key004不会抛异常
    }
}
