/*
 * Copyright (c) 2021 L3Harris Technologies
 */
package com.harris.cinnato.outputs;

import com.typesafe.config.Config;

import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Outputs the messages to a single rotating file log located in `./log/messages.log`
 */
public class FileOutput extends Output {
    private static final Logger logger = LoggerFactory.getLogger("file");

    /**
     * Calls superclass constructor on Config parameter
     *
     * @param config the consumer config properties
     */
    public FileOutput(Config config) {
        super(config);
    }

    /**
     * Logs message to a file
     *
     * @param message the incoming message.
     */
    @Override
    public void output(String message, String header) {
        // if (this.config.getBoolean("headers")) {
        //     logger.info(header + this.convert(message));
        // } else {
        //     logger.info(this.convert(message));
        // }
        String outputMessage = this.config.getBoolean("headers") 
            ? header + this.convert(message) 
            : this.convert(message);

        // Log to file using SLF4J
        logger.info(outputMessage);

        // Additional direct file writing (optional)
        try (FileWriter writer = new FileWriter("log/messages.log", true)) {
            writer.write(outputMessage + System.lineSeparator());
        } catch (IOException e) {
            logger.error("Error writing to log file", e);
        }
    }
}
