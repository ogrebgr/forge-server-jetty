package com.bolyartech.forge.server.jetty;

import com.bolyartech.forge.server.config.ForgeConfigurationException;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ForgeJettyConfigurationLoaderClasspath implements ForgeJettyConfigurationLoader {
    private static final String FILENAME = "conf/jetty.conf";
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ClassLoader classLoader;


    @SuppressWarnings("unused")
    public ForgeJettyConfigurationLoaderClasspath(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    @Override
    public ForgeJettyConfiguration load() throws ForgeConfigurationException {
        InputStream is = classLoader.getResourceAsStream(FILENAME);
        if (is != null) {
            Properties prop = new Properties();
            try {
                prop.load(is);
            } catch (IOException e) {
                logger.error("Cannot load config file", e);
                throw new ForgeConfigurationException(e);
            }

            try {
                return ConfigurationPropertiesHelper.fromProperties(prop);
            } catch (Exception e) {
                logger.error("Error populating Jetty configuration", e);
                throw new ForgeConfigurationException(e);
            }
        } else {
            logger.error("Problem  finding/loading configuration file " + FILENAME);
            throw new ForgeConfigurationException();
        }
    }
}
