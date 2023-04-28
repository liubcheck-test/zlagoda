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
    <title>All Cashiers</title>
</head>
<body>
<h1 class="table_dark">All cashiers:</h1>
<table border="1" class="table_dark">
    <tr>
        <th>ID</th>
        <th>Full Name</th>
        <th>Salary</th>
        <th>Date of birth</th>
        <th>Date of start</th>
        <th>Phone number</th>
        <th>Address</th>
        <th>Email</th>
    </tr>
    <c:forEach var="cashier" items="${cashiers}">
        <tr>
            <td>
                <c:out value="${cashier.id}"/>
            </td>
            <td>
                <c:out value="${cashier.employeeFullName}"/>
            </td>
            <td>
                <fmt:formatNumber value="${cashier.salary}" pattern="#.00"/>
            </td>
            <td>
                <c:out value="${cashier.dateOfBirth}"/>
            </td>
            <td>
                <c:out value="${cashier.dateOfStart}"/>
            </td>
            <td>
                <c:out value="${cashier.phoneNumber}"/>
            </td>
            <td>
                <c:out value="${cashier.address}"/>
            </td>
            <td>
                <c:out value="${cashier.email}"/>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/employees/update?id=${cashier.id}">UPDATE</a>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/employees/delete?id=${cashier.id}">DELETE</a>
            </td>
        </tr>
    </c:forEach>
</table>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>
