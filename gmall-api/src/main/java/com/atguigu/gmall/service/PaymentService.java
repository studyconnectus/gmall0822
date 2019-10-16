package com.atguigu.gmall.service;

/**
 * @author liumw
 * @date 2019/10/16
 * @describe
 */
public interface PaymentService {
    void sendDelayPaymentResultCheckQueue(String orderNo,int delaySec,int checkCount);
}
