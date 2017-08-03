package com.vergilyn.demo.redis.listener.jedis;

import com.vergilyn.demo.redis.config.JedisConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/8/3
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=JedisExpiredApplication.class)
public class JedisExpiredApplicationTest {
    @Autowired
    private Jedis jedis;
    @Autowired
    private JedisExpiredListener expiredListener;
    @Before
    public void before() throws Exception {
        jedis.flushAll();

        jedis.set(JedisConfig.DEFAULE_KEY,"123321");
        System.out.println(JedisConfig.DEFAULE_KEY + " = " + jedis.get(JedisConfig.DEFAULE_KEY));
        System.out.println("set expired 5s");
        jedis.expire(JedisConfig.DEFAULE_KEY,5);
    }

    @Test
    public void testPSubscribe(){
        /* psubscribe是一个阻塞的方法，在取消订阅该频道前，会一直阻塞在这，只有当取消了订阅才会执行下面的other code
         * 可以onMessage/onPMessage里面收到消息后，调用了unsubscribe()/onPUnsubscribe(); 来取消订阅，这样才会执行后面的other code
         */
        jedis.psubscribe(expiredListener,JedisExpiredListener.LISTENER_PATTERN);

        // other code
    }
}