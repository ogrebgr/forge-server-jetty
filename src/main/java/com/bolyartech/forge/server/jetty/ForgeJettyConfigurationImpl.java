package com.bolyartech.forge.server.jetty;


import com.bolyartech.forge.server.config.ForgeConfigurationException;


public class ForgeJettyConfigurationImpl implements ForgeJettyConfiguration {
    private final String host;
    private final int httpPort;
    private final int httpsPort;
    private final String temporaryDirectory;
    private final int sessionTimeout;
    private final int maxRequestSizeSize;
    private final int maxFileUploadSize;
    private final int fileSizeThreshold;
    private final String keyStorePath;
    private final String keyStorePassword;
    private final String trustStorePath;
    private final String trustStorePassword;
    private final String configDir;


    @SuppressWarnings("unused")
    public ForgeJettyConfigurationImpl(String host,
                                       int httpPort,
                                       int httpsPort,
                                       int sessionTimeout,
                                       String temporaryDirectory,
                                       int maxRequestSizeSize,
                                       int maxFileUploadSize,
                                       int fileSizeThreshold,
                                       String keyStorePath,
                                       String keyStorePassword,
                                       String trustStorePath,
                                       String trustStorePassword,
                                       String configDir) throws ForgeConfigurationException {
        this.host = host;
        if (httpPort > 0) {
            if (httpPort == httpsPort) {
                throw new ForgeConfigurationException("HTTP port and HTTPS ports are the same: " + httpPort);
            }
        }
        this.httpPort = httpPort;
        this.httpsPort = httpsPort;
        this.sessionTimeout = sessionTimeout;
        this.temporaryDirectory = temporaryDirectory;
        this.maxRequestSizeSize = maxRequestSizeSize;
        this.maxFileUploadSize = maxFileUploadSize;
        this.fileSizeThreshold = fileSizeThreshold;

        if (this.httpsPort > 0) {
            if (keyStorePath == null || keyStorePath.length() == 0) {
                throw new ForgeConfigurationException("https port is set but no keystore info is provided");
            }
        }
        this.keyStorePath = keyStorePath;
        this.keyStorePassword = keyStorePassword;
        this.trustStorePath = trustStorePath;
        this.trustStorePassword = trustStorePassword;
        this.configDir = configDir;
    }


    @Override
    public String getHost() {
        return host;
    }


    @Override
    public int getHttpPort() {
        return httpPort;
    }


    @Override
    public int getHttpsPort() {
        return httpsPort;
    }


    public String getKeyStorePath() {
        return keyStorePath;
    }


    public String getKeyStorePassword() {
        return keyStorePassword;
    }


    @Override
    public int getSessionTimeout() {
        return sessionTimeout;
    }


    @Override
    public String getTemporaryDirectory() {
        return temporaryDirectory;
    }


    public int getMaxRequestSize() {
        return maxRequestSizeSize;
    }


    @Override
    public int getMaxFileUploadSize() {
        return maxFileUploadSize;
    }


    @Override
    public int getFileSizeThreshold() {
        return fileSizeThreshold;
    }


    @Override
    public String getTrustStorePath() {
        return trustStorePath;
    }


    @Override
    public String getTrustStorePassword() {
        return trustStorePassword;
    }


    @Override
    public String getConfigDir() {
        return configDir;
    }
}
