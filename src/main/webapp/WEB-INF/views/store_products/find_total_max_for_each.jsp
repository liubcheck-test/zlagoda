<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Category Max Price Products</title>
    <style>
        <%@include file='/WEB-INF/views/css/table_dark.css' %>
    </style>
</head>
<body>
<h1>Category Max Price Products</h1>
<table class="table-dark">
    <thead>
    <tr>
        <th>Category Number</th>
        <th>Category Name</th>
        <th>Product ID</th>
        <th>Product Name</th>
        <th>Selling Price</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="categoryMaxPriceProduct" items="${list}">
        <tr>
            <td>${categoryMaxPriceProduct.categoryNumber}</td>
            <td>${categoryMaxPriceProduct.categoryName}</td>
            <td>${categoryMaxPriceProduct.idProduct}</td>
            <td>${categoryMaxPriceProduct.productName}</td>
            <td>${categoryMaxPriceProduct.sellingPrice}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
