package com.squeed.guice.example.business;

import com.google.inject.Inject;

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
