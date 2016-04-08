package com.squeed.guice.example.web.jsp;

import com.google.inject.Injector;
import com.squeed.guice.example.business.Transaction;
import com.squeed.guice.example.web.guice.TransactionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.HttpJspPage;
import java.io.IOException;

public class TransactionPage extends HttpServlet implements HttpJspPage {

    protected <T> T getGuiceInstance(Class<T> clazz) {
        return ((Injector) getServletContext().getAttribute(Injector.class.getName())).getInstance(clazz);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getGuiceInstance(TransactionManager.class).push(new Transaction(getPath(request)));
        _jspService(request, response);
        getGuiceInstance(TransactionManager.class).pop();
    }

    private String getPath(HttpServletRequest request) {
        String includeUri = (String) request.getAttribute("javax.servlet.include.request_uri");
        return request.getServletPath() + (includeUri != null ? " -> " + includeUri : "");
    }

    @Override
    public void _jspService(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

    }

    @Override
    public void jspInit() {

    }

    @Override
    public void jspDestroy() {

    }
}
