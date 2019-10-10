package com.atguigu.gmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.OmsCartItem;
import com.atguigu.gmall.cart.mapper.OmsCartItemMapper;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liumw
 * @date 2019/10/10
 * @describe
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private OmsCartItemMapper cartItemMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public OmsCartItem ifCartExistByUser(Long userId, Long skuId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(userId);
        omsCartItem.setProductSkuId(skuId);
        OmsCartItem omsCartItem1 = cartItemMapper.selectOne(omsCartItem);
        return omsCartItem1;
    }

    @Override
    public void addCart(OmsCartItem cartItem) {
        if(cartItem.getMemberId() != null){
            cartItemMapper.insert(cartItem);
        }
    }

    @Override
    public void updateCart(OmsCartItem omsCartItem) {
        //有才更新
        cartItemMapper.updateByPrimaryKey(omsCartItem);
    }

    @Override
    public void flushCache(Long userId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(userId);
        List<OmsCartItem> select = cartItemMapper.select(omsCartItem);
        Map<String,String> map = new HashMap<>();
        for (OmsCartItem cartItem : select) {
            cartItem.setTotalPrice(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
            map.put(cartItem.getProductSkuId() + "",JSON.toJSONString(cartItem));
        }
        Jedis jedis = redisUtil.getJedis();
        jedis.del("user:"+userId + ":cart");
        jedis.hmset("user:"+userId +  ":cart",map);
        jedis.close();
    }

    @Override
    public List<OmsCartItem> getCartList(Long userId) {
        Jedis jedis = redisUtil.getJedis();
        Map<String, String> map = jedis.hgetAll("user:" + userId + ":cart");
        List<OmsCartItem> collect = map.values().stream().map(x -> JSON.parseObject(x, OmsCartItem.class)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public void checkCart(OmsCartItem omsCartItem) {
        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("memberId",omsCartItem.getMemberId()).andEqualTo("productSkuId",omsCartItem.getProductSkuId());
        cartItemMapper.updateByExampleSelective(omsCartItem,example);

        flushCache(omsCartItem.getMemberId());
    }
}
