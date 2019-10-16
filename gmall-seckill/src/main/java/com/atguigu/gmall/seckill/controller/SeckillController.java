package com.atguigu.gmall.seckill.controller;

import com.atguigu.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * @author liumw
 * @date 2019/10/16
 * @describe
 */
@Controller
public class SeckillController {

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/seckill")
    @ResponseBody
    public String redisKill(){
        Jedis jedis = redisUtil.getJedis();
        jedis.watch("106");
        String s = jedis.get("106");
        if (Long.parseLong(s) > 0){
            Transaction multi = jedis.multi();
            multi.incrBy("106", -1);
            List<Object> exec = multi.exec();
            if (exec != null && exec.size() > 0){
                System.out.println("抢购成功");
            }else{
                System.out.println("抢购失败");
            }
        }
        return "1";
    }
}
