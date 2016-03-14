package com.squeed.guice.example.standalone.guice;

import com.google.inject.Provider;
import com.squeed.guice.example.business.Transaction;

import java.util.concurrent.atomic.AtomicInteger;

// TODO: Make this a SimpleTransactionProvider so it can be used in Standalone and in Web
public class StandaloneTransactionProvider implements Provider<Transaction> {
    private static AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Transaction get() {
        return new Transaction("T#" + counter.incrementAndGet());
    }
}
