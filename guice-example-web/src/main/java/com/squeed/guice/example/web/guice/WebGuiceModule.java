package com.squeed.guice.example.web.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletScopes;
import com.squeed.guice.example.business.Transaction;
import com.squeed.guice.example.web.api.RESTEasyApiService;

public class WebGuiceModule extends AbstractModule {

    private static final Injector injector = Guice.createInjector(new WebGuiceModule());

    @Override
    protected void configure() {
        bind(RESTEasyApiService.class);

        // TODO Why can i never inject this with @Inject in TransactionPage ?!
        bind(TransactionManager.class).in(ServletScopes.REQUEST);

        // First use this and show that we always get new transactions
        bind(Transaction.class).toProvider(WebTransactionProvider.class);

        // Then use this and show that we can bind transaction per request
//        bind(Transaction.class).toProvider(WebTransactionProvider.class).in(ServletScopes.REQUEST);

    }

    public static Injector getInjector() {
        return injector;
    }
}
