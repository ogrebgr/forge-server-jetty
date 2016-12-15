package com.bolyartech.forge.server.jetty;

import java.io.IOException;


public interface ForgeJettyConfigurationLoader {
    ForgeJettyConfiguration load(ClassLoader cl) throws ForgeJettyConfigurationException;}
