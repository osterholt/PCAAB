/*
 * Copyright (c) 2021 L3Harris Technologies
 */
package com.harris.cinnato;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.harris.cinnato.outputs.Output;
import com.harris.cinnato.solace.AMQPConsumer;
import com.harris.cinnato.solace.JMSConsumer;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Main entry-point for starting the JMS Consumer given the configuration loaded @see com.typesafe.config.ConfigFactory.
 * Starts the metrics reporter and connects the JMS Consumer to the message broker.
 */
class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
    private static final MetricRegistry metrics = new MetricRegistry();

    public static void main(String[] args) throws Exception {
        Output reporter = null;
        Config config = ConfigFactory.load();
        String reporterName = config.getString("output");
        try {
            Class<?> clazz = Class.forName(reporterName);
            Object obj = clazz.getConstructor(Config.class).newInstance(config);
            reporter = (Output) obj;
        } catch (Exception e) {
            logger.error("Invalid outputs class provided {}", reporterName);
            System.exit(-1);
        }

        if (config.getString("providerUrl").startsWith("amqp")) {
            AMQPConsumer consumer = new AMQPConsumer(config, metrics, reporter);
            consumer.connect();
        } else {
            JMSConsumer consumer = new JMSConsumer(config, metrics, reporter);
            consumer.connect();
        }

        if (config.getBoolean("metrics")) {
            startReporter();
        }

        while (true) {
            Thread.sleep(20000);
        }
    }

    /**
     * Starts metrics logging for message rate.
     * Sets the Logger where the metrics are logged.
     * Sets the rate at which the metrics are logged.
     */
    private static void startReporter() {
        final Slf4jReporter reporter = Slf4jReporter.forRegistry(metrics)
                .outputTo(LoggerFactory.getLogger("com.harris.cinnato"))
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(1, TimeUnit.SECONDS);
    }

}
