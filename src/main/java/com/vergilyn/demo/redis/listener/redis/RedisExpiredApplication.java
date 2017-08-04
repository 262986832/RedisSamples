package com.vergilyn.demo.redis.listener.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/8/3
 */
@SpringBootApplication
public class RedisExpiredApplication implements CommandLineRunner{
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisExpiredListener expiredListener;

    /**
     * 解决redisTemplate的key/value乱码问题:
     *  <br/> <a href="http://www.zhimengzhe.com/shujuku/other/192111.html">http://www.zhimengzhe.com/shujuku/other/192111.html</a>
     *  <br/> <a href="http://blog.csdn.net/tianyaleixiaowu/article/details/70595073">http://blog.csdn.net/tianyaleixiaowu/article/details/70595073</a>
     * @return
     */
    @Bean("redis")
    @Primary
    public RedisTemplate redisTemplate(){
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory redisConnection, Executor executor){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 设置Redis的连接工厂
        container.setConnectionFactory(redisConnection);
        // 设置监听使用的线程池
//        container.setTaskExecutor(executor);
        // 设置监听的Topic: PatternTopic/ChannelTopic
        Topic topic = new PatternTopic(RedisExpiredListener.LISTENER_PATTERN);
        // 设置监听器
        container.addMessageListener(new RedisExpiredListener(), topic);
        return container;
    }

    @Bean
    public Executor executor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("V-Thread");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }


    public static void main(String[] args) {
        SpringApplication.run(RedisExpiredApplication.class,args);
    }

    @Override
    public void run(String... strings) throws Exception {
        redisTemplate.opsForValue().set("vkey", "vergilyn", 5, TimeUnit.SECONDS);
        System.out.println("init : set vkey vergilyn ex 5");
        System.out.println("thread sleep: 10s");
        Thread.sleep(10 * 1000);
        System.out.println("thread recover: get vkey = " + redisTemplate.opsForValue().get("vkey"));
    }
}
