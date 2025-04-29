/*
 * Copyright (c) 2021 L3Harris Technologies
 */
package com.harris.cinnato.outputs;

import com.typesafe.config.Config;
import org.json.JSONObject;
import org.json.XML;


/**
 * Base class for the output interface of the Consumer @see com.harris.cinnato.Consumer.
 */
public abstract class Output {
    protected final Config config;

    /**
     * Constructor that takes consumer configuration parameters.
     *
     * @param config the consumer config properties
     */
    public Output(Config config) {
        this.config = config;
    }

    /**
     * Implementation of the Consumer output interface.
     *
     * @param message the string to output
     */
    public abstract void output(String message, String header);

    /**
     * Converts a string to json, if this option is enabled in the consumer configuration.
     *
     * @param message the incoming message.
     * @return the message converted to json, or the original message
     */
    protected String convert(String message) {
        if (this.config.getBoolean("json")) {
            JSONObject xmlJSONObj = XML.toJSONObject(message);
            return xmlJSONObj.toString();
        } else {
            return message;
        }
    }
}
