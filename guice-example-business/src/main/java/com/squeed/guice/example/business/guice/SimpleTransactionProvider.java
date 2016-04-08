package com.squeed.guice.example.business.guice;

import com.google.inject.Provider;
import com.squeed.guice.example.business.Transaction;

public class SimpleTransactionProvider implements Provider<Transaction> {

    @Override
    public Transaction get() {
        return new Transaction("Simple");
    }
}
