<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <title>Main Page</title>
</head>
<body>
<form method="post" id="redirect"></form>
<h1 class="body">Welcome to Zlagoda Market!</h1>
<table class="table_dark">
    <c:set var="userRole" value="${sessionScope.chosenEmployee.employeeRole.name}" />
    <tr>
        <th>Redirect to</th>
    </tr>
    <c:if test="${userRole eq 'Manager'}">
        <tr><td><a href="${pageContext.request.contextPath}/employees/add">Add New Employee</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/employees">Display All Employees</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/employees/cashiers">Display All Cashiers</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/employees/find-phone-and-address">Find Employee's Phone And Address</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/categories/add">Add New Category</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/categories">Display All Categories</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/products/add">Add New Product</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/products">Display All Products</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/products/find-by-category">Find All Products By Certain Category</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/store_products/add">Add Product To The Store</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/store_products">Display All Store Products</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/store_products/find-by-upc">Find Store Product By UPC</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/store_products/promotional">Display All Promotional Store Products</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/store_products/non-promotional">Display All Non-Promotional Store Products</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/store_products/find-total-sold-sum-by-cashier">Get Total Amount Of Certain Sold Store Product By Certain Cashier</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/store_products/find-total-sold-sum">Get Total Sum For Certain Period</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/store_products/find-total-sold-amount">Get Total Sold Amount For Certain Product And Period</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/checks/find-all-by-cashier-and-period">Get Total Sum For Certain Cashier And Date Range</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/checks/find-all-by-period">Display All Checks By Certain Date Range</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/customer_cards/add">Add New Customer</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/customer_cards">Display All Customers</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/customer_cards/find-by-percent">Find All Customers By Certain Percent</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/report">Print Report</a></td></tr>
    </c:if>
    <c:if test="${userRole eq 'Cashier'}">
        <tr><td><a href="${pageContext.request.contextPath}/products">Display All Products</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/products/find-by-name">Find Products By Name</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/products/find-by-category">Find All Products By Certain Category</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/store_products">Display All Store Products</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/store_products/promotional">Display All Promotional Store Products</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/store_products/non-promotional">Display All Non-Promotional Store Products</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/store_products/find-by-upc-for-cashier">Find Store Product Info By UPC</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/customer_cards/add">Add New Customer</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/customer_cards">Display All Customers</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/customer_cards/find-by-last-name">Find All Customers By Last Name</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/checks/add">Sell Store Products</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/checks/today">Display All My Today's Checks</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/checks/find-all-mine-by-period">Display All My Checks By Certain Date Range</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/checks/find-by-check-number">Find Check By Certain Check Number</a></td></tr>
        <tr><td><a href="${pageContext.request.contextPath}/employees/me">Get All My Info</a></td></tr>
    </c:if>
</table>
</body>
</html>
