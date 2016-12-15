package com.bolyartech.forge.server.jetty;

public interface ForgeJettyConfiguration {
    String getHost();
    int getHttpPort();
    int getHttpsPort();
    int getSessionTimeout();
    int getMaxFormSize();
    String getKeyStorePath();
    String getKeyStorePassword();
    String getTrustStorePath();
    String getTrustStorePassword();
}
