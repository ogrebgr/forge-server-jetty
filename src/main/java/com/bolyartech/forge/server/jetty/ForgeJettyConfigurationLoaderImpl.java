package com.bolyartech.forge.server.jetty;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ForgeJettyConfigurationLoaderImpl implements ForgeJettyConfigurationLoader {
    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(this.getClass());

    private static final String FILENAME = "jetty.conf";

    private static final String PROP_HOST = "host";
    private static final String PROP_HTTP_PORT = "http_port";
    private static final String PROP_HTTPS_PORT = "https_port";
    private static final String PROP_SESSION_TIMEOUT = "session_timeout_seconds";
    private static final String PROP_MAX_FORM_SIZE = "max_form_size_bytes";
    private static final String PROP_KEYSTORE_PATH = "keystore_path";
    private static final String PROP_KEYSTORE_PASSWORD = "keystore_password";
    private static final String PROP_TRUSTSTORE_PATH = "truststore_path";
    private static final String PROP_TRUSTSTORE_PASSWORD = "truststore_password";



    @Override
    public ForgeJettyConfiguration load(ClassLoader cl) throws ForgeJettyConfigurationException {
        InputStream is = cl.getResourceAsStream(FILENAME);
        if (is != null) {
            Properties prop = new Properties();
            try {
                prop.load(is);

            } catch (IOException e) {
                mLogger.error("Cannot load config file");
                throw new ForgeJettyConfigurationException(e);
            }

            try {
                return new ForgeJettyConfigurationImpl(prop.getProperty(PROP_HOST),
                        Integer.parseInt(prop.getProperty(PROP_HTTP_PORT)),
                        Integer.parseInt(prop.getProperty(PROP_HTTPS_PORT)),
                        Integer.parseInt(prop.getProperty(PROP_SESSION_TIMEOUT)),
                        Integer.parseInt(prop.getProperty(PROP_MAX_FORM_SIZE)),
                        prop.getProperty(PROP_KEYSTORE_PATH),
                        prop.getProperty(PROP_KEYSTORE_PASSWORD),
                        prop.getProperty(PROP_TRUSTSTORE_PATH),
                        prop.getProperty(PROP_TRUSTSTORE_PASSWORD)
                        );
            } catch(Exception e) {
                mLogger.error("Error populating configuration", e);
                throw new ForgeJettyConfigurationException(e);
            }
        } else {
            return null;
        }
    }
}
