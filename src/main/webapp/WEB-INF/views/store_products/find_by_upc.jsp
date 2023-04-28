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
    <title>Find Store Product by UPC</title>
</head>
<body>
<h1 class="table_dark">Find Store Product by UPC</h1>
<form method="post" action="${pageContext.request.contextPath}/store_products/find-by-upc">
    <label for="upc">UPC:</label>
    <select name="upc" id="upc" required>
        <c:forEach var="upc" items="${upcs}">
            <c:set var="selectedUpc" value="${requestScope.selectedUpc}" />
            <option value="${upc}" ${upc == selectedUpc || param.selectedUpc == upc ? 'selected' : ''}>${upc}</option>
        </c:forEach>
    </select>
    <input type="submit" value="Find">
</form>
<c:if test="${not empty errorMessage}">
    <h2 class="table_dark">${errorMessage}</h2>
</c:if>
<c:if test="${not empty product}">
    <h2 class="table_dark">Results for Store Product by UPC ${selectedUpc}:</h2>
    <table border="1" class="table_dark">
        <thead>
        <tr>
            <th>Product Name</th>
            <th>Price</th>
            <th>Amount</th>
            <c:if test="${product.containsKey('characteristics')}">
                <th>Characteristics</th>
            </c:if>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>${product.product_name}</td>
            <td><fmt:formatNumber value="${product.selling_price}" pattern="#.00"/></td>
            <td>${product.products_number}</td>
            <c:if test="${product.containsKey('characteristics')}">
                <td>${product.characteristics}</td>
            </c:if>
        </tr>
        </tbody>
    </table>
</c:if>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>