package com.atguigu.gmall.util;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;

/**
 * @author liumw
 * @date 2019/10/15
 * @describe
 */
public class ActiveMqUtil {

    PooledConnectionFactory connectionFactory = null;

    public void init(String brokerUrl) {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(factory);
        this.connectionFactory = pooledConnectionFactory;
    }

    public PooledConnectionFactory getConnectionFactory(){
        return connectionFactory;
    }
}
