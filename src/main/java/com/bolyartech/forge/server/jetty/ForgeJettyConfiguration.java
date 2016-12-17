package com.bolyartech.forge.server.jetty;

public interface ForgeJettyConfiguration {
    String getHost();
    int getHttpPort();
    int getHttpsPort();
    int getSessionTimeout();
    String getTemporaryDirectory();
    int getMaxRequestSize();
    int getMaxFileUploadSize();
    int getFileSizeThreshold();
    String getKeyStorePath();
    String getKeyStorePassword();
    String getTrustStorePath();
    String getTrustStorePassword();
}
