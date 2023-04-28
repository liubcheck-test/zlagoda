<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <title>Find Sales by Period</title>
</head>
<body>
<h1 class="table_dark">Find Sales by Period</h1>
<form method="post" action="${pageContext.request.contextPath}/store_products/find-total-sold-sum">
    <label for="startDate">Start Date:</label>
    <input type="date" id="startDate" name="startDate" value="${startDate.toLocalDate()}" required>
    <input type="time" id="startTime" name="startTime" value="${startDate.toLocalTime()}" required>
    <br><br>
    <label for="endDate">End Date:</label>
    <input type="date" id="endDate" name="endDate" value="${endDate.toLocalDate()}" required>
    <input type="time" id="endTime" name="endTime" value="${endDate.toLocalTime()}" required>
    <br><br>
    <input type="submit" value="Find">
</form>
<c:if test="${totalSum == 0}">
    <h2 class="table_dark">No Sales Found</h2>
</c:if>
<c:if test="${totalSum > 0}">
    <h3 class="table_dark">Total Sum: ${totalSum}</h3>
</c:if>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>
