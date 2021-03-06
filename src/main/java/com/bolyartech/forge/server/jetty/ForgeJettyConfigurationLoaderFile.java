package com.bolyartech.forge.server.jetty;

import com.bolyartech.forge.server.config.ForgeConfigurationException;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Properties;


public class ForgeJettyConfigurationLoaderFile implements ForgeJettyConfigurationLoader {
    private static final String SYSTEM_PROPERTY_JETTY_CONF_FILE = "jetty_config_file";
    private static final String JETTY_CONF_FILE = "jetty.conf";
    private static final String CONF_DIR = "conf";
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ForgeJettyConfigurationLoaderFile.class);
    private final String configFilePath;


    @SuppressWarnings("unused")
    public ForgeJettyConfigurationLoaderFile(String configFilePath) {
        if (configFilePath == null) {
            throw new NullPointerException("configFilePath is null");
        }

        this.configFilePath = configFilePath;
    }


    @SuppressWarnings("unused")
    public static String detectJettyConfigFilePath() {
        String configFilePath = System.getProperty(SYSTEM_PROPERTY_JETTY_CONF_FILE);

        if (configFilePath == null) {
            String currentDir = Paths.get(".").toAbsolutePath().normalize().toString();
            File confDir = new File(currentDir, CONF_DIR);
            if (confDir.exists() && confDir.isDirectory() && confDir.canRead()) {
                File jettyConfigFile = new File(confDir, JETTY_CONF_FILE);
                if (jettyConfigFile.exists()) {
                    configFilePath = jettyConfigFile.getAbsolutePath();
                }
            }
        } else {
            File jettyConfigFile = new File(configFilePath);
            configFilePath = jettyConfigFile.getAbsolutePath();
        }

        logger.error("Path to jetty.conf not set. Please set environment variable jetty_config_file" +
                " to point to jetty.conf");


        return configFilePath;
    }


    @Override
    public ForgeJettyConfiguration load() throws ForgeConfigurationException {
        File confFile = new File(configFilePath);
        if (confFile.exists()) {
            Properties prop = new Properties();
            try {
                InputStream is = new BufferedInputStream(new FileInputStream(confFile));
                prop.load(is);
                is.close();
            } catch (IOException e) {
                logger.error("Cannot load jetty config file", e);
                throw new ForgeConfigurationException(e);
            }

            logger.info("Using jetty config file: {}", confFile.getAbsolutePath());
            return ConfigurationPropertiesHelper.fromProperties(prop);
        } else {
            throw new IllegalStateException(MessageFormat.format("Cannot find configuration file: {0}",
                    confFile.getAbsolutePath()));
        }
    }
}
