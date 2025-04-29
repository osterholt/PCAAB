/*
 * Copyright (c) 2021 L3Harris Technologies
 */
package com.harris.cinnato.outputs;

import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Outputs the messages to standard out
 *
 */
public class StdoutOutput extends Output {
    private static final Logger logger = LoggerFactory.getLogger("stdout");

    /**
     * Calls superclass constructor on Config parameter
     *
     * @param config the consumer config properties
     */
    public StdoutOutput(Config config) {
        super(config);
    }

    /**
     * Logs message to a stdout
     *
     * @param message the incoming message
     */
    @Override
    public void output(String message, String header) {
        if (this.config.getBoolean("headers")) {
            logger.info(header + this.convert(message));
        } else {
            logger.info(this.convert(message));
        }
    }
}
