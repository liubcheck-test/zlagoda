<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <title>Add Store Product</title>
</head>
<body>
<form method="post" id="store_product" action="${pageContext.request.contextPath}/store_products/add"></form>
<h1 class="table_dark">Add product to the store:</h1>
<table border="1" class="table_dark">
    <tr>
        <th>Product</th>
        <th>Selling Price</th>
        <th>Amount</th>
        <th>Is Promotional</th>
    </tr>
    <tr>
        <td>
            <select name="id_product" form="store_product" required>
                <c:forEach var="product" items="${products}">
                    <option value="${product.id}">${product.id} ${product.name}</option>
                </c:forEach>
            </select>
        </td>
        <td>
            <input type="number" name="selling_price" form="store_product" required>
        </td>
        <td>
            <input type="number" name="products_number" form="store_product" required>
        </td>
        <td>
            <select name="promotional_product" form="store_product" required>
                <option value="true">Yes</option>
                <option value="false">No</option>
            </select>
        </td>
        <td>
            <input type="submit" name="add" form="store_product">
        </td>
    </tr>
</table>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>
