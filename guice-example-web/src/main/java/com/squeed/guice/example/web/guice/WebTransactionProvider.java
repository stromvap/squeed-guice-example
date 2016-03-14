package com.squeed.guice.example.web.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.squeed.guice.example.business.Transaction;

public class WebTransactionProvider implements Provider<Transaction> {

    @Inject
    private TransactionManager transactionManager;

    @Override
    public Transaction get() {
        return transactionManager.peek();
    }
}
