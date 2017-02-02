package com.bolyartech.forge.server.jetty;

import com.bolyartech.forge.server.config.ForgeConfigurationException;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ForgeJettyConfigurationLoaderClasspath implements ForgeJettyConfigurationLoader {
    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(this.getClass());

    private final ClassLoader mClassLoader;

    private static final String FILENAME = "conf/jetty.conf";

    public ForgeJettyConfigurationLoaderClasspath(ClassLoader classLoader) {
        mClassLoader = classLoader;
    }


    @Override
    public ForgeJettyConfiguration load() throws ForgeConfigurationException {
        InputStream is = mClassLoader.getResourceAsStream(FILENAME);
        if (is != null) {
            Properties prop = new Properties();
            try {
                prop.load(is);
            } catch (IOException e) {
                mLogger.error("Cannot load config file", e);
                throw new ForgeConfigurationException(e);
            }

            try {
                return ConfigurationPropertiesHelper.fromProperties(prop);
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
