<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.squeed.guice.example.business.BusinessLogic" %>
<%@ page import="com.squeed.guice.example.business.Transaction" %>
<%@ page extends="com.squeed.guice.example.web.jsp.TransactionPage" %>

<html>
<head>
    <title>Guice Example</title>
</head>
<body>
<h1>Guice Example</h1>

<h2>index.jsp</h2>
<p><%= getGuiceInstance(BusinessLogic.class).toString() %></p>
<p><%= getGuiceInstance(Transaction.class).toString() %></p>

<jsp:include page="sub.jsp"/>

</body>
</html>
