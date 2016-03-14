package com.squeed.guice.example.web.guice;

import com.squeed.guice.example.business.Transaction;

import java.util.ArrayDeque;
import java.util.Deque;

public class TransactionManager {
    private Deque<Transaction> transactions = new ArrayDeque<>();

    public void push(Transaction transaction) {
        transactions.push(transaction);
    }

    public Transaction pop() {
        return transactions.pop();
    }

    public Transaction peek() {
        return this.transactions.peek();
    }
}
