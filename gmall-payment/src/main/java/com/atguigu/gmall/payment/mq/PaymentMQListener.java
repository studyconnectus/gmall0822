package com.atguigu.gmall.payment.mq;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;

/**
 * @author liumw
 * @date 2019/10/15
 * @describe
 */
@Component
public class PaymentMQListener {

    @Autowired
    private PaymentService paymentService;

    @JmsListener(destination = "TEST_DELAY_QUEUE",containerFactory = "jmsQueueListener")
    public void consumePaymentResult(MapMessage mapMessage) throws JMSException {
        int delaySec = mapMessage.getInt("delaySec");
        String outTradeNo = mapMessage.getString("orderNo");
        int checkCount = mapMessage.getInt("checkCount");
        System.out.println("第" + checkCount + "次调用！");
        checkCount -- ;
        if (checkCount > 0){
            paymentService.sendDelayPaymentResultCheckQueue(outTradeNo,delaySec,checkCount);
        }
    }
}
