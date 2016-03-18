package com.squeed.guice.example.business;

import java.util.concurrent.atomic.AtomicInteger;

public class Transaction {
    private static final AtomicInteger count = new AtomicInteger(0);

    private int id;
    private String name;

    public Transaction(String name) {
        this.id = count.incrementAndGet();
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
