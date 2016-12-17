package com.bolyartech.forge.server.jetty;

import com.bolyartech.forge.server.config.ForgeConfigurationException;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ForgeJettyConfigurationLoaderImpl implements ForgeJettyConfigurationLoader {
    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(this.getClass());

    private static final String FILENAME = "conf/jetty.conf";

    private static final String PROP_HOST = "host";
    private static final String PROP_HTTP_PORT = "http_port";
    private static final String PROP_HTTPS_PORT = "https_port";
    private static final String PROP_SESSION_TIMEOUT = "session_timeout_seconds";
    private static final String PROP_TEMPORARY_DIRECTORY = "temporary_directory";
    private static final String PROP_MAX_REQUEST_SIZE = "max_file_upload_size_bytes";
    private static final String PROP_MAX_FILE_UPLOAD_SIZE = "max_request_size_bytes";
    private static final String PROP_FILE_TRESHOLD_SIZE = "file_size_threshold";
    private static final String PROP_KEYSTORE_PATH = "keystore_path";
    private static final String PROP_KEYSTORE_PASSWORD = "keystore_password";
    private static final String PROP_TRUSTSTORE_PATH = "truststore_path";
    private static final String PROP_TRUSTSTORE_PASSWORD = "truststore_password";



    @Override
    public ForgeJettyConfiguration load(ClassLoader cl) throws ForgeConfigurationException {
        InputStream is = cl.getResourceAsStream(FILENAME);
        if (is != null) {
            Properties prop = new Properties();
            try {
                prop.load(is);

            } catch (IOException e) {
                mLogger.error("Cannot load config file");
                throw new ForgeConfigurationException(e);
            }

            try {
                return new ForgeJettyConfigurationImpl(prop.getProperty(PROP_HOST),
                        Integer.parseInt(prop.getProperty(PROP_HTTP_PORT)),
                        Integer.parseInt(prop.getProperty(PROP_HTTPS_PORT)),
                        Integer.parseInt(prop.getProperty(PROP_SESSION_TIMEOUT)),
                        prop.getProperty(PROP_TEMPORARY_DIRECTORY),
                        Integer.parseInt(prop.getProperty(PROP_MAX_REQUEST_SIZE)),
                        Integer.parseInt(prop.getProperty(PROP_MAX_FILE_UPLOAD_SIZE)),
                        Integer.parseInt(prop.getProperty(PROP_FILE_TRESHOLD_SIZE)),
                        prop.getProperty(PROP_KEYSTORE_PATH),
                        prop.getProperty(PROP_KEYSTORE_PASSWORD),
                        prop.getProperty(PROP_TRUSTSTORE_PATH),
                        prop.getProperty(PROP_TRUSTSTORE_PASSWORD)
                        );
            } catch(Exception e) {
                mLogger.error("Error populating Jetty configuration", e);
                throw new ForgeConfigurationException(e);
            }
        } else {
            mLogger.error("Problem  finding/loading configuration file " + FILENAME);
            throw new ForgeConfigurationException();
        }
    }
}
