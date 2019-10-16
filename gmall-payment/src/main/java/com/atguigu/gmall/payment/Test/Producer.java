package com.atguigu.gmall.payment.Test;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.pool.PooledConnectionFactory;

import javax.jms.*;

/**
 * @author liumw
 * @date 2019/10/15
 * @describe
 */
public class Producer {
    public static void main(String[] args) throws JMSException {
        //初始化连接工厂 
        ConnectionFactory connectionFactory = new
                ActiveMQConnectionFactory("tcp://localhost:61616");
        //获得连接
        Connection conn = connectionFactory.createConnection();
        //创建Session，此方法第一个参数表示会话是否在事务中执行，第二个参数设定会话的应答模式
        Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
        //创建队列
        //Destination dest = session.createTopic("MyTopic");
        Queue testTopic1 = session.createQueue("TEST_DELAY_QUEUE");
        //通过session可以创建消息的生产者
        MessageProducer producer = session.createProducer(testTopic1);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);//持久化
        //启动连接 ，连接的启动位置和queue略有不同，需要把配置配完，在启动连接
        conn.start();
        MapMessage mapMessage = new ActiveMQMapMessage();
        mapMessage.setString("orderNo","11111");
        mapMessage.setInt("delaySec",7);
        mapMessage.setInt("checkCount",1);
        mapMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,1000*7);
        producer.send(mapMessage);
        session.commit();
        session.close();
        //关闭mq连接
        conn.close();

    }
}
