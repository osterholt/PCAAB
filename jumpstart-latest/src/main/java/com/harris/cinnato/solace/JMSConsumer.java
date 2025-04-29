/*
 * Copyright (c) 2021 L3Harris Technologies
 */
package com.harris.cinnato.solace;

import com.codahale.metrics.MetricRegistry;
import com.harris.cinnato.outputs.Output;
import com.typesafe.config.Config;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.NamingException;

public class JMSConsumer extends SolaceConsumerV1 {
    private final Context context;

    public JMSConsumer (Config config, MetricRegistry metrics, Output reporter) throws NamingException {
        super(config, metrics, reporter);
        this.context = new SolaceInitialContext(config);
    }

    @Override
    protected Connection getConnection() throws NamingException, JMSException {
        ConnectionFactory conFactory = (ConnectionFactory) this.context.lookup(this.config.getString("connectionFactory"));
        return conFactory.createConnection();
    }

    @Override
    protected Queue getQueue(Session session) throws NamingException {
        return (Queue) this.context.lookup(this.config.getString("queue"));
    }
}
