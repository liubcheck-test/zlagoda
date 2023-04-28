<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_US" />
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <title>Cashier Information</title>
</head>
<body>
<h1>Cashier Information</h1>
<c:if test="${not empty currentCashier}">
    <table border="1" class="table_dark">
        <thead>
        <tr>
            <th>Employee ID</th>
            <th>Full Name</th>
            <th>Salary</th>
            <th>Date of Birth</th>
            <th>Date of Start</th>
            <th>Phone Number</th>
            <th>Address</th>
            <th>Email</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>${currentCashier.id}</td>
            <td>${currentCashier.employeeFullName}</td>
            <td><fmt:formatNumber value="${currentCashier.salary}" pattern="#.00"/></td>
            <td>${currentCashier.dateOfBirth}</td>
            <td>${currentCashier.dateOfStart}</td>
            <td>${currentCashier.phoneNumber}</td>
            <td>${currentCashier.address}</td>
            <td>${currentCashier.email}</td>
        </tr>
        </tbody>
    </table>
</c:if>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>
