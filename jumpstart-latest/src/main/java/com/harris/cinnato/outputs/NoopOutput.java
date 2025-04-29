/*
 * Copyright (c) 2021 L3Harris Technologies
 */
package com.harris.cinnato.outputs;

import com.typesafe.config.Config;

/**
 * Does not output message, this is the default message output for the JMS Consumer.
 */
public class NoopOutput extends Output {

    /**
     * Calls superclass constructor on Config parameter
     *
     * @param config the consumer config properties
     */
    public NoopOutput(Config config) {
        super(config);
    }

    /**
     * Explicitly does nothing, this is the default message output for the JMS Consumer
     *
     * @param message the incoming message.
     */
    @Override
    public void output(String message, String header) {
        // explicitly do nothing
    }
}
