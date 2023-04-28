<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <title>All Categories</title>
</head>
<body>
<h1 class="table_dark">All categories:</h1>
<table border="1" class="table_dark">
    <tr>
        <th>Number</th>
        <th>Name</th>
    </tr>
    <c:forEach var="category" items="${categories}">
        <tr>
            <td>
                <c:out value="${category.categoryNumber}"/>
            </td>
            <td>
                <c:out value="${category.categoryName}"/>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/categories/update?categoryNumber=${category.categoryNumber}">UPDATE</a>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/categories/delete?categoryNumber=${category.categoryNumber}">DELETE</a>
            </td>
        </tr>
    </c:forEach>
</table>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>
