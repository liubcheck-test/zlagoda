<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<form method="post" id="employee" action="${pageContext.request.contextPath}/register"></form>
<h1 class="table_dark">Register employee:</h1>
<table border="1" class="table_dark">
    <tr>
        <th>Surname</th>
        <th>Name</th>
        <th>Patronymic</th>
        <th>Role</th>
        <th>Salary</th>
        <th>Date of birth</th>
        <th>Date of start</th>
        <th>Phone number</th>
        <th>City</th>
        <th>Street</th>
        <th>Zip code</th>
        <th>Email</th>
        <th>Password</th>
    </tr>
    <tr>
        <td>
            <input type="text" name="empl_surname" form="employee" required>
        </td>
        <td>
            <input type="text" name="empl_name" form="employee" required>
        </td>
        <td>
            <input type="text" name="empl_patronymic" form="employee">
        </td>
        <td>
            <select name="empl_role" form="employee" required>
                <c:forEach var="role" items="${employeeRoles}">
                    <option value="${role}">${role.name()}</option>
                </c:forEach>
            </select>
        </td>
        <td>
            <input type="number" name="salary" form="employee" required>
        </td>
        <td>
            <input type="date" name="date_of_birth" form="employee" required>
        </td>
        <td>
            <input type="date" name="date_of_start" form="employee" required>
        </td>
        <td>
            <input type="text" name="phone_number" form="employee" required>
        </td>
        <td>
            <input type="text" name="city" form="employee" required>
        </td>
        <td>
            <input type="text" name="street" form="employee" required>
        </td>
        <td>
            <input type="text" name="zip_code" form="employee">
        </td>
        <td>
            <input type="text" name="email" form="employee" required>
        </td>
        <td>
            <input type="password" name="password" form="employee" required>
        </td>
        <td>
            <input type="submit" name="add" form="employee">
        </td>
    </tr>
</table>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Exit</a></h5>
</body>
</html>
