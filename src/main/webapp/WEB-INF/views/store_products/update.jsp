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
    <title>Update Store Product</title>
</head>
<body>
<form method="post" id="store_product" action="${pageContext.request.contextPath}/store_products/update"></form>
<h1 class="table_dark">Update store product:</h1>
<table border="1" class="table_dark">
    <tr>
        <th>UPC</th>
        <th>UPC Promotional</th>
        <th>Product</th>
        <th>Selling Price</th>
        <th>Amount</th>
        <th>Is Promotional</th>
    </tr>
    <tr>
        <td>
            <input type="text" name="UPC" form="store_product" value="${storeProduct.upc}" readonly>
        </td>
        <td>
            <input type="text" name="UPC_prom" form="store_product" value="${storeProduct.upcProm}" readonly>
        </td>
        <td>
            <select name="id_product" form="store_product" required>
                <c:forEach var="product" items="${products}">
                    <option value="${product.id}" ${product.id == storeProduct.product.id ? 'selected' : ''}>${product.name}</option>
                </c:forEach>
            </select>
        </td>
        <td>
            <input type="text" name="selling_price" form="store_product" value="<fmt:formatNumber value='${storeProduct.price}' pattern='#.00' currencySymbol='.'/>"
                   required pattern="^\d+(\.\d{2})?$" title="Please enter a valid value in the format #.00">
        </td>
        <td>
            <input type="number" name="products_number" form="store_product" value="${storeProduct.amount}" required>
        </td>
        <td>
            <select name="promotional_product" form="store_product" value="${storeProduct.isPromotional}" required>
                <option value="true">Yes</option>
                <option value="false">No</option>
            </select>
        </td>
        <td>
            <input type="submit" name="update" form="store_product">
        </td>
    </tr>
</table>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>
