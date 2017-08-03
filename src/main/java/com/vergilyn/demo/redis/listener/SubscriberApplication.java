package com.vergilyn.demo.redis.listener;

import com.vergilyn.demo.redis.RedisApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.Jedis;

/**
 * 订阅者
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/8/3
 */
@SpringBootApplication(scanBasePackages = {"com.vergilyn.demo.redis.config"})
public class SubscriberApplication implements CommandLineRunner{
    @Autowired
    private Jedis jedis;

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        /* patterns参考redis.conf中的"EVENT NOTIFICATION"说明
         * #  K     Keyspace events, published with __keyspace@<db>__ prefix.
         * #  E     Keyevent events, published with __keyevent@<db>__ prefix.
         */
        jedis.psubscribe(new KeyExpiredListener(), "__key*__:*");
    }
}
