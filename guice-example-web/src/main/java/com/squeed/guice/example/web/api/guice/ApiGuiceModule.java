package com.squeed.guice.example.web.api.guice;

import com.google.inject.AbstractModule;
import com.google.inject.servlet.ServletScopes;
import com.squeed.guice.example.business.Transaction;
import com.squeed.guice.example.business.guice.SimpleTransactionProvider;
import com.squeed.guice.example.web.api.RESTEasyApiService;

public class ApiGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RESTEasyApiService.class);
        bind(Transaction.class).toProvider(SimpleTransactionProvider.class).in(ServletScopes.REQUEST);
    }
}
