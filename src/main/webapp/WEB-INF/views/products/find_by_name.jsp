<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <title>Products</title>
</head>
<body>
<h1 class="table_dark">Products</h1>
<form method="get" action="${pageContext.request.contextPath}/products/find-by-name">
    <label for="name">Name:</label>
    <input type="text" id="name" name="name" value="${param.name}" required>
    <br><br>
    <input type="submit" value="Search">
</form>
<c:if test="${empty products}">
    <h2 class="table_dark">No Products Found</h2>
</c:if>
<c:if test="${not empty products}">
    <table border="1" class="table_dark">
        <thead>
        <tr>
            <th>Category</th>
            <th>Name</th>
            <th>Characteristics</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>${product.category.categoryName}</td>
                <td>${product.name}</td>
                <td>${product.characteristics}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>
