package com.bolyartech.forge.server.jetty;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.http.HttpServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.session.SessionDataStoreFactory;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ForgeJetty {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ForgeJettyConfiguration forgeJettyConfiguration;
    private final HttpServlet mainServlet;
    private final SessionDataStoreFactory sessionDataStoreFactory;
    private Server server;


    public ForgeJetty(ForgeJettyConfiguration conf, HttpServlet mainServlet) {
        forgeJettyConfiguration = conf;
        this.mainServlet = mainServlet;
        sessionDataStoreFactory = null;
    }


    public ForgeJetty(ForgeJettyConfiguration forgeJettyConfiguration, HttpServlet mainServlet, SessionDataStoreFactory sessionDataStoreFactory) {
        this.forgeJettyConfiguration = forgeJettyConfiguration;
        this.mainServlet = mainServlet;
        this.sessionDataStoreFactory = sessionDataStoreFactory;
    }


    @SuppressWarnings("unused")
    public void start() {
        server = new Server();
        setConnectors(server, forgeJettyConfiguration);

        if (sessionDataStoreFactory != null) {
            server.addBean(sessionDataStoreFactory);
        }

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.getSessionHandler().setMaxInactiveInterval(forgeJettyConfiguration.getSessionTimeout());
        context.setMaxFormContentSize(forgeJettyConfiguration.getMaxRequestSize());
        context.setContextPath("/");
        ServletHolder holder = new ServletHolder(mainServlet);
        logger.info("Session timeout: {} seconds", forgeJettyConfiguration.getSessionTimeout());

        holder.getRegistration().setMultipartConfig(new MultipartConfigElement(forgeJettyConfiguration.getTemporaryDirectory(),
                forgeJettyConfiguration.getMaxFileUploadSize(),
                forgeJettyConfiguration.getMaxRequestSize(),
                forgeJettyConfiguration.getFileSizeThreshold()));
        context.addServlet(holder, "/*");

        server.setHandler(context);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            logger.error("Error starting the server: ", e);
            try {
                server.stop();
            } catch (Exception e1) {
                //suppress
            }
        }
    }


    @SuppressWarnings("unused")
    public void stop() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void setConnectors(Server server, ForgeJettyConfiguration conf) {

        List<Connector> mConnectors = new ArrayList<>();

        if (conf.getHttpPort() > 0) {
            ServerConnector connector = new ServerConnector(this.server);
            connector.setHost(conf.getHost());
            connector.setPort(conf.getHttpPort());
            mConnectors.add(connector);
            logger.info("Listening HTTP on {}, port {}", conf.getHost(), conf.getHttpPort());
        }

        if (conf.getHttpsPort() > 0) {
            File f = new File(conf.getKeyStorePath());
            if (!f.exists()) {
                logger.error("Cannot find SSL keystore file at: " + conf.getKeyStorePath());
                throw new IllegalStateException("Cannot find SSL keystore file at: " + conf.getKeyStorePath());
            }
            SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
            sslContextFactory.setKeyStorePath(conf.getKeyStorePath());
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
            logger.info("Listening HTTPS on {}, port {}", conf.getHost(), conf.getHttpsPort());
        }

        this.server.setConnectors(mConnectors.toArray(new Connector[]{}));
    }


}
