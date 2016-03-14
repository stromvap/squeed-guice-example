package com.squeed.guice.example.business;

import com.google.inject.Inject;

public class BusinessLogic {

    @Inject
    private Transaction transaction;

    public String getBusiness() {
        return "In BusinessLogic with Transaction name: " + transaction.getName();
    }
}
