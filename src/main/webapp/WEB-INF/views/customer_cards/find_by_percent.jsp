<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<style>
  <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
  <title>Find Customers by Card Percentage</title>
</head>
<body>
<h1 class="table_dark">Find Customers by Card Percentage</h1>
<form method="post" action="${pageContext.request.contextPath}/customer_cards/find-by-percent">
  <label for="percent">Card Percentage:</label>
  <select name="percent" id="percent" required>
    <c:forEach var="p" items="${percentages}">
      <option value="${p}" ${percent == p || param.percent == p ? 'selected' : ''}>${p}%</option>
    </c:forEach>
  </select>
  <input type="submit" value="Find">
</form>
<c:if test="${not empty errorMessage}">
  <h2 class="table_dark">${errorMessage}</h2>
</c:if>
<c:if test="${not empty customers}">
  <h2 class="table_dark">Results for Customers:</h2>
  <table border="1" class="table_dark">
    <thead>
    <tr>
      <th>Full Name</th>
      <th>Phone Number</th>
      <th>Address</th>
      <th>Card Percentage</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="customer" items="${customers}">
      <tr>
        <td>${customer.customerFullName}</td>
        <td>${customer.phoneNumber}</td>
        <td>${customer.customerAddress}</td>
        <td>${customer.percent}%</td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</c:if>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>