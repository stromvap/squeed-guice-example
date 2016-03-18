package com.squeed.guice.example.thread.scope;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class ThreadGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bindScope(ThreadScoped.class, CustomScopes.THREAD);
        bind(ThreadCache.class).in(Scopes.SINGLETON);
    }
}
