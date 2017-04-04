package com.vergilyn.demo.redis.TypeOperate;

import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @Date 2017/4/2
 */
public abstract class TypeOperateObject {
    private final String localAddr = "127.0.0.1";
    private final int localPort = 6379;

    protected Jedis jedis;//非切片的客户端连接
    protected JedisPool jedisPool;//非切片连接池

    protected ShardedJedis shardedJedis;//切片的客户端连接
    protected ShardedJedisPool shardedJedisPool;//切片连接池

    public TypeOperateObject() {
        initialPool();
        jedis = jedisPool.getResource();

        initialShardedPool();
        shardedJedis = shardedJedisPool.getResource();
    }

    /**
     * 初始化非切片池
     */
    protected void initialPool() {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
       // config.setMaxActive(20);  //jedis高版本中不存在该方法
        config.setMaxTotal(20);
        config.setMaxIdle(5);
       // config.setMaxWait(1000l);
        config.setMaxWaitMillis(1000L);
        config.setTestOnBorrow(false);

        jedisPool = new JedisPool(config, localAddr, localPort);
    }

    /**
     * 初始化切片池
     */
    protected void initialShardedPool() {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(1000L);
        config.setTestOnBorrow(false);

        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo(localAddr, localPort, "master"));

        // 构造池
        shardedJedisPool = new ShardedJedisPool(config, shards);
    }

    public abstract void show() ;


}
