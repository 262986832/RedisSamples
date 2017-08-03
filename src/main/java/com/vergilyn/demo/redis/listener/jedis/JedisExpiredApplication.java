package com.vergilyn.demo.redis.listener.jedis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 测试参考junit
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/8/3
 */
@SpringBootApplication(scanBasePackages = {"com.vergilyn.demo.redis.config","com.vergilyn.demo.redis.listener.jedis"})
public class JedisExpiredApplication{
    public static void main(String[] args) {
        SpringApplication.run(JedisExpiredApplication.class,args);
    }
}
