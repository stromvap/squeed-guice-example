<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.squeed.guice.example.business.BusinessLogic" %>
<%@ page import="com.squeed.guice.example.business.Transaction" %>
<%@ page extends="com.squeed.guice.example.web.jsp.TransactionPage" %>

<div style="margin-left: 20px">
    <h2>sub.jsp</h2>
    <p><%= getGuiceInstance(BusinessLogic.class).toString() %></p>
    <p><%= getGuiceInstance(Transaction.class).toString() %></p>
</div>