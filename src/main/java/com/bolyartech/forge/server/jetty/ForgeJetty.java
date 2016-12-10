package com.bolyartech.forge.server.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;


abstract public class ForgeJetty {
    private Server mServer;

    public void start() {
        mServer = new Server(8080);

        HandlerCollection handlerCollection = new HandlerCollection();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(new ServletHolder(createMainServlet()),"/*");
        mServer.setHandler(context);

        try {
            mServer.start();
            mServer.join();
        } catch (Exception e) {
            e.printStackTrace();
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
