package com.vergilyn.demo.redis.listener;

import redis.clients.jedis.JedisPubSub;

/**
 * redis key失效监听
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/8/3
 */
//@Component
public class KeyExpiredListener extends JedisPubSub {

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        System.out.println("onPSubscribe "
                + pattern + " " + subscribedChannels);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {

        System.out.println("onPMessage pattern "
                + pattern + " " + channel + " " + message);
    }
}
