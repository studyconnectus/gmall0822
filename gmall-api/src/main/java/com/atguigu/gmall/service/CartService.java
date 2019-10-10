package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.OmsCartItem;

import java.util.List;

/**
 * @author liumw
 * @date 2019/10/9
 * @describe
 */
public interface CartService {
    OmsCartItem ifCartExistByUser(Long userId, Long skuId);

    void addCart(OmsCartItem cartItem);

    void updateCart(OmsCartItem omsCartItem);

    void flushCache(Long userId);

    List<OmsCartItem> getCartList(Long userId);

    void checkCart(OmsCartItem omsCartItem);
}
