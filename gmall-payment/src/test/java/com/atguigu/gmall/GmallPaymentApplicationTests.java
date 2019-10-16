package com.atguigu.gmall;

import com.atguigu.gmall.util.ActiveMqUtil;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallPaymentApplicationTests {

    @Autowired
    private ActiveMqUtil activeMqUtil;

    @Test
    public void contextLoads() throws JMSException {

        PooledConnectionFactory connectionFactory = activeMqUtil.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();
        Queue testQueue1 = session.createQueue("testTopic1");
        MessageConsumer consumer = session.createConsumer(testQueue1);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage){
                    try {
                        System.out.println(((TextMessage)message).getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



}
