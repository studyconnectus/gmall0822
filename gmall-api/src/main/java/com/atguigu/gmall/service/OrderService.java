package com.atguigu.gmall.service;

/**
 * @author liumw
 * @date 2019/10/14
 * @describe
 */
public interface OrderService {

    String getTradeCode(Long memberId);

    String checkTradeCode(Long memberId,String tradeCode);
}
