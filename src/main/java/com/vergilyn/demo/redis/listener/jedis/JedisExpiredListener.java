package com.vergilyn.demo.redis.listener.jedis;

import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPubSub;

/**
 * key过期事件推送到topic中只有key，无value，因为一旦过期，value就不存在了。
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/8/3
 */
@Component
public class JedisExpiredListener extends JedisPubSub {
    /** 参考redis目录下redis.conf中的"EVENT NOTIFICATION", redis默认的db{0, 15}一共16个数据库
     * K    Keyspace events, published with __keyspace@<db>__ prefix.
     * E    Keyevent events, published with __keyevent@<db>__ prefix.
     *
     */
    public final static String LISTENER_PATTERN = "__keyevent@*__:expired";
    /**
     * 初始化按表达式的方式订阅时候的处理
     * @param pattern
     * @param subscribedChannels
     */
    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        System.out.print("onPSubscribe >> ");
        System.out.println(String.format("pattern: %s, subscribedChannels: %d", pattern, subscribedChannels));
    }

    /**
     * 取得按表达式的方式订阅的消息后的处理
     * @param pattern
     * @param channel
     * @param message
     */
    @Override
    public void onPMessage(String pattern, String channel, String message) {
        System.out.print("onPMessage >> ");
        System.out.println(String.format("key: %s, pattern: %s, channel: %s", message, pattern, channel));
    }

    /**
     * 取得订阅的消息后的处理
     * @param channel
     * @param message
     */
    @Override
    public void onMessage(String channel, String message) {
        super.onMessage(channel, message);
    }

    /**
     * 初始化订阅时候的处理
     * @param channel
     * @param subscribedChannels
     */
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        super.onSubscribe(channel, subscribedChannels);
    }

    /**
     * 取消订阅时候的处理
     * @param channel
     * @param subscribedChannels
     */
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        super.onUnsubscribe(channel, subscribedChannels);
    }

    /**
     * 取消按表达式的方式订阅时候的处理
     * @param pattern
     * @param subscribedChannels
     */
    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        super.onPUnsubscribe(pattern, subscribedChannels);
    }
}
