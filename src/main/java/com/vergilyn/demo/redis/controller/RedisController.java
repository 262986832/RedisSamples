package com.vergilyn.demo.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/4/3
 */
@Controller
@RequestMapping("/redis")
public class RedisController{
    @Autowired
    private StringRedisTemplate redis;

    @GetMapping({"/index","/",""})
    public String index(){
        return "redis_index";
    }


    public Map<String,Object> keyOperate(){

        return null;
    }
}
