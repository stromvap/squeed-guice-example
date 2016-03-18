package com.squeed.guice.example.standalone.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.squeed.guice.example.business.Transaction;
import com.squeed.guice.example.business.guice.SimpleTransactionProvider;
import com.squeed.guice.example.thread.scope.CustomScopes;
import com.squeed.guice.example.thread.scope.ThreadGuiceModule;

public class StandaloneGuiceModule extends AbstractModule {

    private static final Injector injector = Guice.createInjector(new ThreadGuiceModule(), new StandaloneGuiceModule());

    @Override
    protected void configure() {
        bind(Transaction.class).toProvider(SimpleTransactionProvider.class).in(CustomScopes.THREAD);
    }

    public static Injector getInjector() {
        return injector;
    }

}
