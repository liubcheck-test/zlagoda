<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <title>Find Products by Category</title>
</head>
<body>
<h1 class="table_dark">Find Products by Category</h1>
<form method="post" action="${pageContext.request.contextPath}/products/find-by-category">
    <label for="category">Category:</label>
    <select name="category" id="category" required>
        <c:forEach var="category" items="${categories}">
            <option value="${category}" ${category == categoryName || param.category == category ? 'selected' : ''}>${category}</option>
        </c:forEach>
    </select>
    <input type="submit" value="Find">
</form>
<c:if test="${not empty errorMessage}">
    <h2 class="table_dark">${errorMessage}</h2>
</c:if>
<c:if test="${not empty products}">
    <h2 class="table_dark">Results for Products in Category ${categoryName}:</h2>
    <table border="1" class="table_dark">
        <thead>
        <tr>
            <th>ID</th>
            <th>Product Name</th>
            <th>Characteristics</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>${product.id}</td>
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