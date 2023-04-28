<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_US" />
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <title>My Checks By Date Range</title>
</head>
<body>
<h1 class="table_dark">Find All My Checks By Date Range</h1>
<form method="post" action="${pageContext.request.contextPath}/checks/find-all-mine-by-period">
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
<c:if test="${empty checks}">
    <h2 class="table_dark">No Checks Found</h2>
</c:if>
<c:if test="${not empty checks}">
    <h2 class="table_dark">Checks Found:</h2>
    <table border="1" class="table_dark">
        <thead>
        <tr>
            <th>Print Date</th>
            <th>Customer Card</th>
            <th>Sum Total</th>
            <th>Details</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="check" items="${checks}">
            <tr>
                <td>${check.printDate}</td>
                <td>${check.customerCard.customerFullName} (${check.customerCard.percent}%)</td>
                <td><fmt:formatNumber value="${check.sumTotal}" pattern="#,##0.00"/></td>
                <td><a href="${pageContext.request.contextPath}/checks/get?checkNumber=${check.checkNumber}">Details</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>
