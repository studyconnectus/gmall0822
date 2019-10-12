package com.atguigu.gmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.user.mapper.MemberMapper;
import com.atguigu.gmall.service.MemberService;
import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author liumw
 * @date 2019/8/22
 * @describe
 */
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<UmsMember> getAllUsers() {
        return memberMapper.selectAll();
    }

    @Override
    public UmsMember login(UmsMember umsMember) {

        //查询缓存
        try(Jedis jedis = redisUtil.getJedis()) {
            String userInfo = jedis.get("user:" + umsMember.getUsername() + ":" + umsMember.getPassword() + ":info");
            if (StringUtils.isNotBlank(userInfo)){
                UmsMember umsMemberFromCache = JSON.parseObject(userInfo, UmsMember.class);
                return umsMemberFromCache;
            }else {
                // 从数据库获取用户信息
                return getumsMemberFromDB(umsMember);
            }
        }catch (Exception e){
            // 从数据库获取用户信息
            return getumsMemberFromDB(umsMember);
        }
    }

    private UmsMember getumsMemberFromDB(UmsMember umsMember) {
        UmsMember umsMemberFromDB = memberMapper.selectOne(umsMember);
        if (umsMemberFromDB != null){
            try(Jedis jedis = redisUtil.getJedis()) {
                jedis.setex("user:" + umsMember.getUsername() + ":" + umsMember.getPassword() + ":info",60*60*24,JSON.toJSONString(umsMemberFromDB));
            }
        }
        return umsMemberFromDB;
    }
}
