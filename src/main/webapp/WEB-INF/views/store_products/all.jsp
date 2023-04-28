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
    <title>All Store Products</title>
</head>
<body>
<h1 class="table_dark">All store products:</h1>
<table border="1" class="table_dark">
    <tr>
        <th>UPC</th>
        <th>UPC Promotional</th>
        <th>Product</th>
        <th>Selling price</th>
        <th>Amount</th>
        <th>Is Promotional</th>
    </tr>
    <c:forEach var="storeProduct" items="${storeProducts}">
        <tr>
            <td>
                <c:out value="${storeProduct.upc}"/>
            </td>
            <td>
                <c:out value="${storeProduct.upcProm}"/>
            </td>
            <td>
                <c:out value="${storeProduct.product}"/>
            </td>
            <td>
                <fmt:formatNumber value="${storeProduct.price}" pattern="#.00"/>
            </td>
            <td>
                <c:out value="${storeProduct.amount}"/>
            </td>
            <td>
                <c:out value="${storeProduct.isPromotional}"/>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/store_products/update?upc=${storeProduct.upc}">UPDATE</a>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/store_products/delete?upc=${storeProduct.upc}">DELETE</a>
            </td>
        </tr>
    </c:forEach>
</table>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>
