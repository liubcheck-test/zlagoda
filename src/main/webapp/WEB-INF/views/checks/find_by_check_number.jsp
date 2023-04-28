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
<form method="post" action="${pageContext.request.contextPath}/checks/find-by-check-number">
    <label for="checkNumber">UPC:</label>
    <select name="checkNumber" id="checkNumber" required>
        <c:forEach var="checkNumber" items="${checkNumbers}">
            <c:set var="selectedCheckNumber" value="${requestScope.selectedCheckNumber}" />
            <option value="${checkNumber}" ${checkNumber == selectedCheckNumber
            || param.selectedCheckNumber == checkNumber ? 'selected' : ''}>${checkNumber}</option>
        </c:forEach>
    </select>
    <input type="submit" value="Find">
</form>
<c:if test="${not empty errorMessage}">
    <h2 class="table_dark">${errorMessage}</h2>
</c:if>
<c:if test="${not empty check}">
    <h2 class="table_dark">Results for Check by Check Number ${selectedCheckNumber}:</h2>
    <table border="1" class="table_dark">
        <thead>
        <tr>
            <th>Print Date</th>
            <th>Cashier</th>
            <th>Customer Card</th>
            <th>Sum Total</th>
            <th>Details</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>${check.printDate}</td>
            <td>${check.employee.employeeFullName}</td>
            <td>${check.customerCard.customerFullName} (${check.customerCard.percent}%)</td>
            <td><fmt:formatNumber value="${check.sumTotal}" pattern="#,##0.00"/></td>
            <td><a href="${pageContext.request.contextPath}/checks/get?checkNumber=${check.checkNumber}">Details</a></td>
        </tr>
        </tbody>
    </table>
</c:if>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>