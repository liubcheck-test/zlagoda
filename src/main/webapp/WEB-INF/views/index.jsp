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
<h1 class="body">Hello world!</h1>
<table class="table_dark">
    <tr>
        <th>Redirect to</th>
    </tr>
    <tr><td><a href="${pageContext.request.contextPath}/employees/add">Add New Employee</a></td></tr>
    <tr><td><a href="${pageContext.request.contextPath}/employees">Display All Employees</a></td></tr>
</table>
</body>
</html>
