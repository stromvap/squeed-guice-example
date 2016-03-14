<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.squeed.guice.example.business.BusinessLogic" %>
<%@ page import="com.squeed.guice.example.business.Transaction" %>
<%@ page extends="com.squeed.guice.example.web.jsp.TransactionPage" %>

<h2>menu.jsp</h2>
<span><%= getGuiceInstance(BusinessLogic.class).getBusiness() %></span>
<span><%= getGuiceInstance(Transaction.class).getTransaction() %></span>
