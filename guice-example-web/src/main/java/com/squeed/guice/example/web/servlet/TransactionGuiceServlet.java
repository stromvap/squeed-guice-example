package com.squeed.guice.example.web.servlet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.squeed.guice.example.business.Transaction;
import com.squeed.guice.example.web.guice.TransactionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class TransactionGuiceServlet extends HttpServlet {

    @Inject
    private Provider<TransactionManager> transactionManagerProvider;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        transactionManagerProvider.get().push(new Transaction("Servlet"));
        super.service(req, resp);
        transactionManagerProvider.get().pop();
    }
}
