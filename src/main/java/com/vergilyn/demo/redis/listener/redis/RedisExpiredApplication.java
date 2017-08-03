package com.vergilyn.demo.redis.listener.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/8/3
 */
@SpringBootApplication
public class RedisExpiredApplication {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisExpiredListener expiredListener;

    @Bean
    public RedisMessageListenerContainer listenerContainer(Executor executor){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 设置Redis的连接工厂
        container.setConnectionFactory(redisTemplate.getConnectionFactory());
        // 设置监听使用的线程池
        container.setTaskExecutor(executor);
        // 设置监听的Topic
        ChannelTopic channelTopic = new ChannelTopic(RedisExpiredListener.LISTENER_PATTERN);
        // 设置监听器
        container.addMessageListener(expiredListener, channelTopic);
        return container;
    }

    @Bean
    public Executor executor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("VergiLyn-Thread");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }


    public static void main(String[] args) {
        SpringApplication.run(RedisExpiredApplication.class,args);
    }
}
