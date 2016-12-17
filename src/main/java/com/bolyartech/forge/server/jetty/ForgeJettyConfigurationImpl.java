package com.bolyartech.forge.server.jetty;


import com.bolyartech.forge.server.config.ForgeConfigurationException;


public class ForgeJettyConfigurationImpl implements ForgeJettyConfiguration {
    private final String mHost;
    private final int mHttpPort;
    private final int mHttpsPort;
    private final String mTemporaryDirectoty;
    private final int mSessionTimeout;
    private final int mMaxRequestSizeSize;
    private final int mMaxFileUploadSize;
    private final int mFileSizeThreshold;
    private final String mKeyStorePath;
    private final String mKeyStorePassword;
    private final String mTrustStorePath;
    private final String mTrustStorePassword;


    public ForgeJettyConfigurationImpl(String host,
                                       int httpPort,
                                       int httpsPort,
                                       int sessionTimeout,
                                       String temporaryDirectoty,
                                       int maxRequestSizeSize,
                                       int maxFileUploadSize,
                                       int fileSizeThreshold,
                                       String keyStorePath,
                                       String keyStorePassword,
                                       String trustStorePath,
                                       String trustStorePassword) throws ForgeConfigurationException {
        mHost = host;
        if (httpPort > 0) {
            if (httpPort == httpsPort) {
                throw new ForgeConfigurationException("HTTP port and HTTPS ports are the same: " + httpPort);
            }
        }
        mHttpPort = httpPort;
        mHttpsPort = httpsPort;
        mSessionTimeout = sessionTimeout;
        mTemporaryDirectoty = temporaryDirectoty;
        mMaxRequestSizeSize = maxRequestSizeSize;
        mMaxFileUploadSize = maxFileUploadSize;
        mFileSizeThreshold = fileSizeThreshold;

        if (mHttpsPort > 0) {
            if (keyStorePath == null || keyStorePath.length() == 0) {
                throw new ForgeConfigurationException("https port is set but no keystore info is provided");
            }
        }
        mKeyStorePath = keyStorePath;
        mKeyStorePassword = keyStorePassword;
        mTrustStorePath = trustStorePath;
        mTrustStorePassword = trustStorePassword;
    }


    @Override
    public String getHost() {
        return mHost;
    }


    @Override
    public int getHttpPort() {
        return mHttpPort;
    }


    @Override
    public int getHttpsPort() {
        return mHttpsPort;
    }


    public String getKeyStorePath() {
        return mKeyStorePath;
    }


    public String getKeyStorePassword() {
        return mKeyStorePassword;
    }


    @Override
    public int getSessionTimeout() {
        return mSessionTimeout;
    }


    @Override
    public String getTemporaryDirectory() {
        return mTemporaryDirectoty;
    }


    public int getMaxRequestSize() {
        return mMaxRequestSizeSize;
    }


    @Override
    public int getMaxFileUploadSize() {
        return mMaxFileUploadSize;
    }


    @Override
    public int getFileSizeThreshold() {
        return mFileSizeThreshold;
    }


    @Override
    public String getTrustStorePath() {
        return mTrustStorePath;
    }


    @Override
    public String getTrustStorePassword() {
        return mTrustStorePassword;
    }
}
