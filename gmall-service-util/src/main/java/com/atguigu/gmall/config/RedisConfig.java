package com.atguigu.gmall.config;

import com.atguigu.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liumw
 * @date 2019/9/7
 * @describe
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host:disabled}")

    public String host;

    @Value("${spring.redis.port:0}")
    public int port;

    @Value("${spring.redis.database:0}")
    public int database;

    @Value("${spring.redis.password}")
    public String password;

    @Bean
    public RedisUtil getRedisUtil(){
        if ("disabled".equals(host)){
            return null;
        }
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.initPool(host,port,database,password);
        return redisUtil;
    }
}
