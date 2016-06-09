package com.squeed.guice.example.web.guice;

import com.squeed.guice.example.business.Transaction;

import java.util.ArrayDeque;
import java.util.Deque;

public class TransactionManager {
    private Deque<Transaction> transactions = new ArrayDeque<>();

    public void push(Transaction transaction) {
        transactions.push(transaction);
        System.out.println("Push Size: " + transactions.size());
    }

    public Transaction pop() {
        Transaction pop = transactions.pop();
        System.out.println("Pop Size:  " + transactions.size());
        return pop;
    }

    public Transaction peek() {
        return this.transactions.peek();
    }
}
