package com.atguigu.gmall.payment.Test;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author liumw
 * @date 2019/10/15
 * @describe
 */
public class FeiLong {

    public static void main(String[] args) {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        try {
            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("test");
            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if (message instanceof TextMessage){
                        try {
                            String text = ((TextMessage) message).getText();
                            System.out.println("让我来我是飞龙" + text);
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
