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


public class ForgeJetty {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ForgeJettyConfiguration forgeJettyConfiguration;

    private Server server;
    private final HttpServlet mainServlet;


    public ForgeJetty(ForgeJettyConfiguration conf, HttpServlet mainServlet) {
        forgeJettyConfiguration = conf;
        this.mainServlet = mainServlet;
    }



    @SuppressWarnings("unused")
    public void start() {
        server = new Server();
        setConnectors(server, forgeJettyConfiguration);


        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.getSessionHandler().setMaxInactiveInterval(forgeJettyConfiguration.getSessionTimeout());
        context.setMaxFormContentSize(forgeJettyConfiguration.getMaxRequestSize());
        context.setContextPath("/");
        ServletHolder holder = new ServletHolder(mainServlet);

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
        }

        if (conf.getHttpsPort() > 0) {
            File f = new File(conf.getKeyStorePath());
            if (!f.exists()) {
                logger.error("Cannot find SSL keystore file at: " + conf.getKeyStorePath());
                throw new IllegalStateException("Cannot find SSL keystore file at: " + conf.getKeyStorePath());
            }
            SslContextFactory sslContextFactory = new SslContextFactory.Server();
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
        }

        this.server.setConnectors(mConnectors.toArray(new Connector[]{}));
    }


}
