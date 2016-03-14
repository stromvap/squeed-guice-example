package com.squeed.guice.example.standalone.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.squeed.guice.example.business.Transaction;
import com.squeed.guice.example.thread.scope.CustomScopes;
import com.squeed.guice.example.thread.scope.ThreadCache;
import com.squeed.guice.example.thread.scope.ThreadScoped;

public class StandaloneGuiceModule extends AbstractModule {

    private static final Injector injector = Guice.createInjector(new StandaloneGuiceModule());

    @Override
    protected void configure() {
        bindScope(ThreadScoped.class, CustomScopes.THREAD);
        bind(ThreadCache.class).in(Scopes.SINGLETON);
        bind(Transaction.class).toProvider(StandaloneTransactionProvider.class).in(CustomScopes.THREAD);
    }

    public static Injector getInjector() {
        return injector;
    }

}
