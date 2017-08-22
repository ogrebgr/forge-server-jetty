package com.bolyartech.forge.server.jetty;

import com.bolyartech.forge.server.config.ForgeConfigurationException;


public interface ForgeJettyConfigurationLoader {
    @SuppressWarnings("unused")
    ForgeJettyConfiguration load() throws ForgeConfigurationException;
}
