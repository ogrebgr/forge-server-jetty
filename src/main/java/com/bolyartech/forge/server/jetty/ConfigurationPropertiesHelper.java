package com.bolyartech.forge.server.jetty;

import com.bolyartech.forge.server.config.ForgeConfigurationException;

import java.util.Properties;


public class ConfigurationPropertiesHelper {
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
    private static final String PROP_CONFIG_DIR = "config_dir";

    public static ForgeJettyConfigurationImpl fromProperties(Properties prop) throws ForgeConfigurationException {
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
                prop.getProperty(PROP_TRUSTSTORE_PASSWORD),
                prop.getProperty(PROP_CONFIG_DIR)
        );
    }
}
