package com.squeed.guice.example.web.servlet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.squeed.guice.example.business.BusinessLogic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Singleton
public class GuiceServlet extends TransactionGuiceServlet {

    @Inject
    private Provider<BusinessLogic> businessLogicProvider;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter printWriter = resp.getWriter();
        printWriter.append("In Servlet: ").append(businessLogicProvider.get().toString());
        printWriter.flush();
        printWriter.close();
    }
}
