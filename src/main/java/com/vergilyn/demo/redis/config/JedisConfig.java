package com.vergilyn.demo.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/4/4
 */
@Configuration
@EnableConfigurationProperties(JedisProperties.class)
public class JedisConfig {
    public final static String DEFAULE_KEY = "vkey";
    @Autowired
    private JedisProperties jedisProperties;

    @Bean
    public JedisPoolConfig initJedisPoolConfig(){
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(jedisProperties.getMaxTotal());
        config.setMaxIdle(jedisProperties.getMaxIdle());
        config.setMaxWaitMillis(jedisProperties.getMaxWaitMillis());
        config.setTestOnBorrow(jedisProperties.isTestOnBorrow());
        return config;
    }

    @Bean
    public JedisPool initJedisPool(JedisPoolConfig jedisPoolConfig){
        return new JedisPool(jedisPoolConfig, jedisProperties.getHost(),jedisProperties.getPort());
    }

    @Bean
    @Primary
    public Jedis initJedis(JedisPool jedisPool){
        return jedisPool.getResource();
    }

//  通过AutoConfiguration获取。
//  @Bean
//  @Qualifier("AutoJedis")
    public Jedis autoJedis(JedisConnectionFactory factory){
		return new JedisPool(factory.getPoolConfig(),jedisProperties.getHost(),jedisProperties.getPort())
                        .getResource();
	}
}
