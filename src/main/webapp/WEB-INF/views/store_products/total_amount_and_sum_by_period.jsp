<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <title>Total Amount and Sum by Period</title>
</head>
<body>
<h1>Total Amount and Sum by Period</h1>
<form method="post" action="${pageContext.request.contextPath}/store_products/total_amount_and_sum_by_period">
    <label for="start_date">Start date:</label>
    <input type="datetime-local" id="start_date" name="start_date"><br><br>
    <label for="end_date">End date:</label>
    <input type="datetime-local" id="end_date" name="end_date"><br><br>
    <input type="submit" value="Submit">
</form>
<br>
<c:if test="${not empty productDetailsList}">
    <table class="table_dark">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Characteristics</th>
            <th>Category</th>
            <th>UPC</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="productDetails" items="${productDetailsList}">
            <tr>
                <td>${productDetails.product.id}</td>
                <td>${productDetails.product.name}</td>
                <td>${productDetails.product.characteristics}</td>
                <td>${productDetails.product.category.categoryName}</td>
                <td>${productDetails.upc}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
</body>
</html>
