<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <title>All employees</title>
</head>
<body>
<h1 class="table_dark">All employees:</h1>
<table border="1" class="table_dark">
    <tr>
        <th>ID</th>
        <th>Full Name</th>
        <th>Role</th>
        <th>Salary</th>
        <th>Date of birth</th>
        <th>Date of start</th>
        <th>Phone number</th>
        <th>Address</th>
        <th>Email</th>
        <th>Delete</th>
    </tr>
    <c:forEach var="employee" items="${employees}">
        <tr>
            <td>
                <c:out value="${employee.id}"/>
            </td>
            <td>
                <c:out value="${employee.employeeFullName}"/>
            </td>
            <td>
                <c:out value="${employee.employeeRole.name}"/>
            </td>
            <td>
                <c:out value="${employee.salary}"/>
            </td>
            <td>
                <c:out value="${employee.dateOfBirth}"/>
            </td>
            <td>
                <c:out value="${employee.dateOfStart}"/>
            </td>
            <td>
                <c:out value="${employee.phoneNumber}"/>
            </td>
            <td>
                <c:out value="${employee.address}"/>
            </td>
            <td>
                <c:out value="${employee.email}"/>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/employees/update?id=${employee.id}">UPDATE</a>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/employees/delete?id=${employee.id}">DELETE</a>
            </td>
        </tr>
    </c:forEach>
</table>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>
