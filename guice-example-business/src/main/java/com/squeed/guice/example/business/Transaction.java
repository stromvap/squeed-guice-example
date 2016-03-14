package com.squeed.guice.example.business;

public class Transaction {
    private String name;

    public Transaction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getTransaction() {
        return "In Transaction with name: " + name;
    }
}
