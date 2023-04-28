<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
  <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
  <title>Customer Cards</title>
</head>
<body>
<h1 class="table_dark">Customer Cards</h1>
<form method="get" action="${pageContext.request.contextPath}/customer_cards/find-by-last-name">
  <label for="lastName">Last Name:</label>
  <input type="text" id="lastName" name="lastName" value="${param.lastName}" required>
  <br><br>
  <input type="submit" value="Search">
</form>
<c:if test="${empty customerCards}">
  <h2 class="table_dark">No Customer Cards Found</h2>
</c:if>
<c:if test="${not empty customerCards}">
  <table border="1" class="table_dark">
    <thead>
    <tr>
      <th>Card Number</th>
      <th>Full Name</th>
      <th>Phone Number</th>
      <th>Address</th>
      <th>Discount Percent</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="customerCard" items="${customerCards}">
      <tr>
        <td>${customerCard.cardNumber}</td>
        <td>${customerCard.customerFullName}</td>
        <td>${customerCard.phoneNumber}</td>
        <td>${customerCard.customerAddress}</td>
        <td>${customerCard.percent}%</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</c:if>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>
