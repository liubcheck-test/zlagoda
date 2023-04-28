<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <title>All customer cards</title>
</head>
<body>
<h1 class="table_dark">All customer cards:</h1>
<table border="1" class="table_dark">
    <c:set var="userRole" value="${sessionScope.chosenEmployee.employeeRole.name}" />
    <tr>
        <th>ID</th>
        <th>Full Name</th>
        <th>Phone number</th>
        <th>Address</th>
        <th>Percent</th>
    </tr>
    <c:forEach var="cashier" items="${customer_cards}">
        <tr>
            <td>
                <c:out value="${cashier.cardNumber}"/>
            </td>
            <td>
                <c:out value="${cashier.customerFullName}"/>
            </td>
            <td>
                <c:out value="${cashier.phoneNumber}"/>
            </td>
            <td>
                <c:out value="${cashier.customerAddress}"/>
            </td>
            <td>
                <c:out value="${cashier.percent}"/>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/customer_cards/update?cardNumber=${cashier.cardNumber}">UPDATE</a>
            </td>
            <c:if test="${userRole eq 'Manager'}">
            <td>
                <a href="${pageContext.request.contextPath}/customer_cards/delete?cardNumber=${cashier.cardNumber}">DELETE</a>
            </td>
            </c:if>
        </tr>
    </c:forEach>
</table>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>
