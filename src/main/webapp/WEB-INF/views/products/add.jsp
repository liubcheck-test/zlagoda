<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <title>Add Product</title>
</head>
<body>
<form method="post" id="product" action="${pageContext.request.contextPath}/products/add"></form>
<h1 class="table_dark">Add product:</h1>
<table border="1" class="table_dark">
    <tr>
        <th>Category</th>
        <th>Product Name</th>
        <th>Characteristics</th>
    </tr>
    <tr>
        <td>
            <select name="category_number" form="product" required>
                <c:forEach var="category" items="${categories}">
                    <option value="${category.categoryNumber}">${category.categoryNumber} ${category.categoryName}</option>
                </c:forEach>
            </select>
        </td>
        <td>
            <input type="text" name="product_name" form="product">
        </td>
        <td>
            <input type="text" name="characteristics" form="product">
        </td>
        <td>
            <input type="submit" name="add" form="product">
        </td>
    </tr>
</table>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>
