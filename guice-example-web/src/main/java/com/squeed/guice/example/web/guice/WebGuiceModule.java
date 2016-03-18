package com.squeed.guice.example.web.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletScopes;
import com.squeed.guice.example.business.Transaction;

public class WebGuiceModule extends AbstractModule {

    private static final Injector injector = Guice.createInjector(new WebGuiceModule());

    @Override
    protected void configure() {
        bind(TransactionManager.class).in(ServletScopes.REQUEST);
        bind(Transaction.class).toProvider(WebTransactionProvider.class);

//        TODO: Explain why we should not bind this with Request scope
//        bind(Transaction.class).toProvider(WebTransactionProvider.class).in(ServletScopes.REQUEST);

    }

    public static Injector getInjector() {
        return injector;
    }
}
