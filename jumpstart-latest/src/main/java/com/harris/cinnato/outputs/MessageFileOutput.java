/*
 * Copyright (c) 2021 L3Harris Technologies
 */
package com.harris.cinnato.outputs;

import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Outputs each message to a separate file located in `./log/`.
 * The files are named using the current millisecond:
 * ./log/1548108867040
 * ./log/1548108881503
 */
public class MessageFileOutput extends Output {
    private static final Logger logger = LoggerFactory.getLogger(MessageFileOutput.class);
    private final File outputDirectory;

    /**
     * Calls superclass constructor on Config parameter, as well set the output directory to the `./log/` directory.
     *
     * @param config the consumer config properties
     */
    public MessageFileOutput(Config config) {
        super(config);
        outputDirectory = new File("./log/");
    }

    /**
     * Outputs each message to a separate file located in `./log/`.
     * the files are named using the current millisecond:
     * ./log/1548108867040
     * ./log/1548108881503
     *
     * @param message the incoming message.
     */
    @Override
    public void output(String message, String header) {
        File file = new File(outputDirectory + File.separator + System.currentTimeMillis());
        try (FileOutputStream stream = new FileOutputStream(file)) {
            if (this.config.getBoolean("headers")) {
                stream.write(header.getBytes());
            }
            stream.write(this.convert(message).getBytes());
        } catch (FileNotFoundException e) {
            logger.error("File not found", e);
        } catch (IOException e) {
            logger.error("Failed to close the file", e);
        }
    }
}
