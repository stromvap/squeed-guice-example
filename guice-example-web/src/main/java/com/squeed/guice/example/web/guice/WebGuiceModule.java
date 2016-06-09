package com.squeed.guice.example.web.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.ServletScopes;
import com.squeed.guice.example.business.Transaction;
import com.squeed.guice.example.web.servlet.GuiceServlet;

public class WebGuiceModule extends AbstractModule {

    private static final Injector injector = Guice.createInjector(new WebGuiceModule(), new ServletModule() {
        @Override
        protected void configureServlets() {
            serve("/guice-servlet").with(GuiceServlet.class);
        }
    });

    @Override
    protected void configure() {
        bind(TransactionManager.class).in(ServletScopes.REQUEST);
        bind(Transaction.class).toProvider(WebTransactionProvider.class);
    }

    public static Injector getInjector() {
        return injector;
    }
}
