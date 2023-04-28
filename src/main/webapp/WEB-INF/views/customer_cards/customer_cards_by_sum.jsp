<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <meta charset="UTF-8">
    <title>Customer Cards by Sum</title>
</head>
<body>
<h1>Customer Cards by Sum</h1>
<form method="post" action="${pageContext.request.contextPath}/customer_cards/customer_cards_by_sum">
    <label for="inputSum">Input Sum:</label>
    <input type="number" step="0.01" name="inputSum" id="inputSum">
    <button type="submit">Submit</button>
</form>
<c:if test="${not empty customerCards}">
    <table class="table_dark">
        <tr>
            <th>Card Number</th>
            <th>Surname</th>
            <th>Name</th>
            <th>Patronymic</th>
            <th>Phone Number</th>
            <th>Discount Percent</th>
        </tr>
        <c:forEach var="customerCard" items="${customerCards}">
            <tr>
                <td>${customerCard.cardNumber}</td>
                <td>${customerCard.customerFullName.surname}</td>
                <td>${customerCard.customerFullName.name}</td>
                <td>${customerCard.customerFullName.patronymic}</td>
                <td>${customerCard.phoneNumber}</td>
                <td>${customerCard.percent}</td>
            </tr>
        </c:forEach>
    </table>
</c:if>
</body>
</html>
