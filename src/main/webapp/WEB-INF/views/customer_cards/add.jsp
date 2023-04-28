<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <title>Add Customer Card</title>
</head>
<body>
<form method="post" id="customer_card" action="${pageContext.request.contextPath}/customer_cards/add"></form>
<h1 class="table_dark">Add customer card:</h1>
<table border="1" class="table_dark">
    <tr>
        <th>Surname</th>
        <th>Name</th>
        <th>Patronymic</th>
        <th>Phone number</th>
        <th>City</th>
        <th>Street</th>
        <th>Zip code</th>
        <th>Percent</th>
    </tr>
    <tr>
        <td>
            <input type="text" name="cust_surname" form="customer_card" required>
        </td>
        <td>
            <input type="text" name="cust_name" form="customer_card" required>
        </td>
        <td>
            <input type="text" name="cust_patronymic" form="customer_card">
        </td>
        <td>
            <input type="text" name="phone_number" form="customer_card" required>
        </td>
        <td>
            <input type="text" name="city" form="customer_card" required>
        </td>
        <td>
            <input type="text" name="street" form="customer_card" required>
        </td>
        <td>
            <input type="text" name="zip_code" form="customer_card">
        </td>
        <td>
            <input type="number" name="percent" form="customer_card" required>
        </td>
        <td>
            <input type="submit" name="add" form="customer_card">
        </td>
    </tr>
</table>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>
