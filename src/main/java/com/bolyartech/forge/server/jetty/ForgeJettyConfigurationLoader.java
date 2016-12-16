package com.bolyartech.forge.server.jetty;

import com.bolyartech.forge.server.config.ForgeConfigurationException;

import java.io.IOException;


public interface ForgeJettyConfigurationLoader {
    ForgeJettyConfiguration load(ClassLoader cl) throws ForgeConfigurationException;}
