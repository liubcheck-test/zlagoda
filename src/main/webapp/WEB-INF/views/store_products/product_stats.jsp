<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_US" />
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <meta charset="UTF-8">
    <title>Product Stats</title>
</head>
<body>
<h1>Product Stats</h1>
<table class="table_dark">
    <thead>
    <tr>
        <th>Product ID</th>
        <th>Product Name</th>
        <th>Characteristics</th>
        <th>Total Number Sold</th>
        <th>Total Sales</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${productStats}" var="productStat">
        <tr>
            <td>${productStat.idProduct}</td>
            <td>${productStat.productName}</td>
            <td>${productStat.characteristics}</td>
            <td>${productStat.totalNumber}</td>
            <td>${productStat.totalSum}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
