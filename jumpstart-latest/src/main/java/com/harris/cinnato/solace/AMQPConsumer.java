/*
 * Copyright (c) 2021 L3Harris Technologies
 */
package com.harris.cinnato.solace;

import com.codahale.metrics.MetricRegistry;
import com.harris.cinnato.outputs.Output;
import com.typesafe.config.Config;

import org.apache.qpid.jms.JmsConnectionFactory;

import jakarta.jms.Connection;
import jakarta.jms.JMSException;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import javax.naming.NamingException;

public class AMQPConsumer extends SolaceConsumerV2 {
    public AMQPConsumer (Config config, MetricRegistry metrics, Output reporter) throws NamingException {
        super(config, metrics, reporter);
    }

    @Override
    protected Connection getConnection() throws JMSException {
        String username = this.config.getString("username");
        String password = this.config.getString("password");
        String providerUrl = this.config.getString("providerUrl");
        return new JmsConnectionFactory(username, password, providerUrl).createConnection();
    }

    @Override
    protected Queue getQueue(Session session) throws Exception {
        return session.createQueue(this.config.getString("queue"));
    }
}
