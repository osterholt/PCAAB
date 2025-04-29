/*
 * Copyright (c) 2021 L3Harris Technologies
 */
package com.harris.cinnato.solace;

import com.solacesystems.jms.SupportedProperty;
import com.typesafe.config.Config;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

/**
 * Creates a naming context from Solace connection configuration.
 */
class SolaceInitialContext extends InitialContext{

    SolaceInitialContext(Config config) throws NamingException {
        String providerUrl = config.getString("providerUrl");
        String username = config.getString("username");
        String password = config.getString("password");
        String vpn = config.getString("vpn");
        String truststorePath = SolaceInitialContext.class.getClassLoader().getResource("cacerts").toExternalForm();

        Hashtable<String, Object> env = new Hashtable <>();
        env.put(InitialContext.INITIAL_CONTEXT_FACTORY, "com.solacesystems.jndi.SolJNDIInitialContextFactory");
        env.put(InitialContext.PROVIDER_URL, providerUrl);
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(SupportedProperty.SOLACE_JMS_SSL_VALIDATE_CERTIFICATE, true);
        env.put(SupportedProperty.SOLACE_JMS_VPN, vpn);
        env.put(SupportedProperty.SOLACE_JMS_SSL_TRUST_STORE, truststorePath);
        env.put(SupportedProperty.SOLACE_JMS_JNDI_CONNECT_RETRIES, -1);
        this.init(env);
    }
}
