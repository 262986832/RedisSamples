package com.vergilyn.demo.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/4/4
 */
@Configuration
public class ShardedJedisConfig{
    @Autowired
    private JedisProperties jedisProperties;

    @Bean
    public ShardedJedisPool initShardedJedisPool(JedisPoolConfig jedisPoolConfig){
        // slave链接
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo(jedisProperties.getHost(), jedisProperties.getPort(), "master"));
        return new ShardedJedisPool(jedisPoolConfig,shards);
    }

    @Bean
    public ShardedJedis initShardedJedis(ShardedJedisPool shardedJedisPool){
        return shardedJedisPool.getResource();
    }
}
