package com.atguigu.gmall.redission;

import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

/**
 * @author liumw
 * @date 2019/9/20
 * @describe
 */
@RestController
public class RedissonTest {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedissonClient redissonClient;

    @RequestMapping("/test")
    public String testRedisson(){
        RLock lock = redissonClient.getLock("lock");
        Jedis jedis = redisUtil.getJedis();
        lock.lock();
        try {
            String k = "k";
            String v = jedis.get(k);
            System.out.println(v);
            if (StringUtils.isBlank(v)){
                v = "1";
            }
            jedis.set(k,(Integer.parseInt(v) + 1) + "");
        } finally {
            lock.unlock();
            jedis.close();
        }
        return "success";
    }


}
