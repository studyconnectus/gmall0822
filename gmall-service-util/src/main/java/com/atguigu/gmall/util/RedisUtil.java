package com.atguigu.gmall.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author liumw
 * @date 2019/9/7
 * @describe
 */
public class RedisUtil {

    private JedisPool jedisPool;

    public void initPool(String host,int port,int database,String password){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(200);
        config.setMaxIdle(30);
        config.setBlockWhenExhausted(true);
        config.setMaxWaitMillis(10*1000);
        config.setTestOnBorrow(true);
        jedisPool = new JedisPool(config,host,port,20*1000,password);
    }

    public Jedis getJedis(){
        return jedisPool.getResource();
    }
}
