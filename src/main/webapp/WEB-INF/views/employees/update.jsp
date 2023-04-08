<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<style>
    <%@include file='/WEB-INF/views/css/table_dark.css' %>
</style>
<html>
<head>
    <title>Update Employee</title>
</head>
<body>
<form method="post" id="employee" action="${pageContext.request.contextPath}/employees/update"></form>
<h1 class="table_dark">Update employee:</h1>
<table border="1" class="table_dark">
    <tr>
        <th>ID</th>
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
            <input type="text" name="id" form="employee" value="${employee.id}" readonly>
        </td>
        <td>
            <input type="text" name="empl_surname" form="employee" value="${employee.employeeFullName.surname}" required>
        </td>
        <td>
            <input type="text" name="empl_name" form="employee" value="${employee.employeeFullName.name}" required>
        </td>
        <td>
            <input type="text" name="empl_patronymic" form="employee" value="${employee.employeeFullName.patronymic}">
        </td>
        <td>
            <select name="empl_role" form="employee" required>
                <c:forEach var="role" items="${employeeRoles}">
                    <option value="${role}" ${role == employee.employeeRole ? 'selected' : ''}>${role.name()}</option>
                </c:forEach>
            </select>
        </td>
        <td>
            <input type="number" name="salary" form="employee" value="${employee.salary}" required>
        </td>
        <td>
            <input type="date" name="date_of_birth" form="employee" value="${employee.dateOfBirth}" required>
        </td>
        <td>
            <input type="date" name="date_of_start" form="employee" value="${employee.dateOfStart}" required>
        </td>
        <td>
            <input type="text" name="phone_number" form="employee" value="${employee.phoneNumber}" required>
        </td>
        <td>
            <input type="text" name="city" form="employee" value="${employee.address.city}" required>
        </td>
        <td>
            <input type="text" name="street" form="employee" value="${employee.address.street}" required>
        </td>
        <td>
            <input type="text" name="zip_code" form="employee" value="${employee.address.zipCode}" required>
        </td>
        <td>
            <input type="text" name="email" form="employee" value="${employee.email}" required>
        </td>
        <td>
            <input type="password" name="password" form="employee" value="${employee.password}" required>
        </td>
        <td>
            <input type="submit" name="update" form="employee">
        </td>
    </tr>
</table>
<h5 class="table_dark"><a href="${pageContext.request.contextPath}/logout">Log out</a></h5>
</body>
</html>
