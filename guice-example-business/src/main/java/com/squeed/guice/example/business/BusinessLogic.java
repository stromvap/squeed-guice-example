package com.squeed.guice.example.business;

import com.google.inject.Inject;

// Dummed down OrderFetcher
public class BusinessLogic {

    @Inject
    private Transaction transaction;

    @Override
    public String toString() {
        return "BusinessLogic{" +
                "transaction=" + transaction +
                '}';
    }
}
