package com.bolyartech.forge.server.jetty;

public class ForgeJettyConfigurationException extends Exception {
    public ForgeJettyConfigurationException() {
    }


    public ForgeJettyConfigurationException(String message) {
        super(message);
    }


    public ForgeJettyConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }


    public ForgeJettyConfigurationException(Throwable cause) {
        super(cause);
    }


    public ForgeJettyConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
