package com.atguigu.gmall.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.service.PaymentService;
import com.atguigu.gmall.util.ActiveMqUtil;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.*;

/**
 * @author liumw
 * @date 2019/10/16
 * @describe
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private ActiveMqUtil activeMqUtil;

    @Override
    public void sendDelayPaymentResultCheckQueue(String orderNo,int delaySec,int checkCount) {
        PooledConnectionFactory connectionFactory = activeMqUtil.getConnectionFactory();
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
            Queue test_delay_queue = session.createQueue("TEST_DELAY_QUEUE");
            MessageProducer producer = session.createProducer(test_delay_queue);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            MapMessage mapMessage = new ActiveMQMapMessage();
            mapMessage.setString("orderNo",orderNo);
            mapMessage.setInt("delaySec",delaySec);
            mapMessage.setInt("checkCount",checkCount);
            mapMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,1000*delaySec);

            producer.send(mapMessage);
            session.commit();
            producer.close();
            session.close();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
