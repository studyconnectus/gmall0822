package com.atguigu.gmall.payment.Test;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author liumw
 * @date 2019/10/15
 * @describe
 */
public class Mingwei {
    public static void main(String[] args) {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        try {
            Connection connection = factory.createConnection();
            connection.setClientID("mingwei1");
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic testTopci = session.createTopic("MyTopic");

            MessageConsumer consumer = session.createDurableSubscriber(testTopci,"mingwei1");
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if (message instanceof TextMessage){
                        try {
                            String text = ((TextMessage) message).getText();
                            System.out.println("让我来，我哦是明伟" + text);
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            while (true){

            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
