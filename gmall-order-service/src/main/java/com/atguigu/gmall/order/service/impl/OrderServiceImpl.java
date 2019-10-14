package com.atguigu.gmall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.service.OrderService;
import com.atguigu.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * @author liumw
 * @date 2019/10/14
 * @describe
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String getTradeCode(Long memberId) {
        String tradeCode = "";
        try(Jedis jedis = redisUtil.getJedis()) {
            tradeCode = UUID.randomUUID().toString();
            jedis.setex("user:" + memberId + ":tradeCode",60 * 30,tradeCode);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tradeCode;
    }

    @Override
    public String checkTradeCode(Long memberId, String tradeCode) {
        String tradeKey = "user:" + memberId + ":tradeCode";
        try(Jedis jedis = redisUtil.getJedis()) {
            String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
            Long eval = (Long)jedis.eval(script, Collections.singletonList(tradeKey), Collections.singletonList(tradeCode));
            if (eval != null && eval != 0){
                return "success";
            }else {
                return "fail";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "fail";
    }
}
