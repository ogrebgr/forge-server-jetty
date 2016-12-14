package com.bolyartech.forge.server.jetty;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;


abstract public class ForgeJetty {
    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(this.getClass());
    private Server mServer;

    public void start() {
        mServer = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(new ServletHolder(createMainServlet()),"/*");
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

    abstract public HttpServlet createMainServlet();
}
