package com.squeed.guice.example.web.guice;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

public class WebGuiceServletContextListener extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return WebGuiceModule.getInjector();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext sc = servletContextEvent.getServletContext();
        sc.setAttribute(Injector.class.getName(), getInjector());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext sc = servletContextEvent.getServletContext();
        sc.removeAttribute(Injector.class.getName());
        super.contextDestroyed(servletContextEvent);
    }
}
