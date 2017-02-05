package com.bolyartech.forge.server.jetty;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.LoggerFactory;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


abstract public class ForgeJetty {
    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(this.getClass());
    private final ForgeJettyConfiguration mForgeJettyConfiguration;

    private Server mServer;


    public ForgeJetty(ForgeJettyConfiguration conf) {
        mForgeJettyConfiguration = conf;
    }


    public void start() {
        mServer = new Server();
        setConnectors(mServer, mForgeJettyConfiguration);


        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.getSessionHandler().getSessionManager().setMaxInactiveInterval(mForgeJettyConfiguration.getSessionTimeout());
        context.setMaxFormContentSize(mForgeJettyConfiguration.getMaxRequestSize());
        context.setContextPath("/");
        ServletHolder holder = new ServletHolder(createMainServlet(mForgeJettyConfiguration.getConfigDir()));

        holder.getRegistration().setMultipartConfig(new MultipartConfigElement(mForgeJettyConfiguration.getTemporaryDirectory(),
                mForgeJettyConfiguration.getMaxFileUploadSize(),
                mForgeJettyConfiguration.getMaxRequestSize(),
                mForgeJettyConfiguration.getFileSizeThreshold()));
        context.addServlet(holder, "/*");

        mServer.setHandler(context);

        try {
            mServer.start();
            mServer.join();
        } catch (Exception e) {
            mLogger.error("Error starting the server: ", e);
            try {
                mServer.stop();
            } catch (Exception e1) {
                //suppress
            }
        }
    }


    public void stop() {
        if (mServer != null) {
            try {
                mServer.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    abstract public HttpServlet createMainServlet(String configDir);


    private void setConnectors(Server server, ForgeJettyConfiguration conf) {

        List<Connector> mConnectors = new ArrayList<>();

        if (conf.getHttpPort() > 0) {
            ServerConnector connector = new ServerConnector(mServer);
            connector.setHost(conf.getHost());
            connector.setPort(conf.getHttpPort());
            mConnectors.add(connector);
        }

        if (conf.getHttpsPort() > 0) {
            File f = new File(conf.getKeyStorePath());
            if (!f.exists()) {
                mLogger.error("Cannot find SSL keystore file at: " + conf.getKeyStorePath());
                throw new IllegalStateException("Cannot find SSL keystore file at: " + conf.getKeyStorePath());
            }
            SslContextFactory sslContextFactory = new SslContextFactory(conf.getKeyStorePath());

            if (conf.getKeyStorePassword() != null) {
                sslContextFactory.setKeyStorePassword(conf.getKeyStorePassword());
            }

            if (conf.getTrustStorePath() != null) {
                sslContextFactory.setTrustStorePath(conf.getTrustStorePath());
            }

            if (conf.getKeyStorePassword() != null) {
                sslContextFactory.setTrustStorePassword(conf.getKeyStorePassword());
            }

            ServerConnector connector = new ServerConnector(server, sslContextFactory);
            connector.setHost(conf.getHost());
            connector.setPort(conf.getHttpsPort());
            mConnectors.add(connector);
        }

        mServer.setConnectors(mConnectors.toArray(new Connector[]{}));
    }


}
