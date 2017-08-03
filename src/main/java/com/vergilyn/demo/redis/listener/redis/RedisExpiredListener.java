package com.vergilyn.demo.redis.listener.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/8/3
 */
@Component
public class RedisExpiredListener implements MessageListener {
    public final static String LISTENER_PATTERN = "__keyevent@*__:expired";
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        // 客户端监听订阅的topic，当有消息的时候，会触发该方法
        byte[] body = message.getBody();// 请使用valueSerializer
        byte[] channel = message.getChannel();
        String topic = new String(channel);
        String itemValue = new String(body);
        // 请参考配置文件，本例中key，value的序列化方式均为string。
        System.out.println("topic:"+topic);
        System.out.println("itemValue:"+itemValue);
    }
}
