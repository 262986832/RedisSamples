package com.vergilyn.demo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

@SpringBootApplication
@EnableCaching	//允许缓存 （此demo允许redis缓存）
public class RedisApplication{
	@Autowired
	private Jedis jedis;
	@Autowired
	private ShardedJedis shardedJedis;
	@Autowired
	private StringRedisTemplate redis;


	@Bean
	@Primary
	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}


	public static void main(String[] args) {
		SpringApplication.run(RedisApplication.class, args);
	}


}
