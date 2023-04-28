<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <title>All Products</title>
</head>
<body>
<h1 class="table_dark">All products:</h1>
<table border="1" class="table_dark">
<c:set var="userRole" value="${sessionScope.chosenEmployee.employeeRole.name}" />
    <tr>
        <th>ID</th>
        <th>Category</th>
        <th>Name</th>
        <th>Characteristics</th>
    </tr>
    <c:forEach var="product" items="${products}">
        <tr>
            <td>
                <c:out value="${product.id}"/>
            </td>
            <td>
                <c:out value="${product.category}"/>
            </td>
            <td>
                <c:out value="${product.name}"/>
            </td>
            <td>
                <c:out value="${product.characteristics}"/>
            </td>
            <c:if test="${userRole eq 'Manager'}">
            <td>
                <a href="${pageContext.request.contextPath}/products/update?id=${product.id}">UPDATE</a>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/products/delete?id=${product.id}">DELETE</a>
            </td>
            </c:if>
        </tr>
    </c:forEach>
</table>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>
