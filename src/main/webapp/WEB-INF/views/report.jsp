<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <title>Report</title>
</head>
<body>
<h1 class="body">Choose What You Report</h1>
<table class="table_dark">
    <c:set var="userRole" value="${sessionScope.chosenEmployee.employeeRole.name}" />
    <tr><td><a href="${pageContext.request.contextPath}/report/employees">Employees</a></td></tr>
    <tr><td><a href="${pageContext.request.contextPath}/report/categories">Categories</a></td></tr>
    <tr><td><a href="${pageContext.request.contextPath}/report/products">Products</a></td></tr>
    <tr><td><a href="${pageContext.request.contextPath}/report/store_products">Store Products</a></td></tr>
    <tr><td><a href="${pageContext.request.contextPath}/report/customer_cards">Customer Cards</a></td></tr>
    <tr><td><a href="${pageContext.request.contextPath}/report/checks">Checks</a></td></tr>
</table>
</body>
</html>
