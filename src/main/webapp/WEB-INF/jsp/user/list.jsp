<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>List123123</title>
</head>
<body>
<spring:url value="/user/add" var="addURL" />
<a href="${addURL }">Add user</a>

<h1>List User</h1>
<table border="1">
	<thead>
		<tr>
			<th>Name</th>
			<th colspan="2">Action</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${listUser }" var="user">
			<tr>
				<td>${user.name }</td>
				<td>
					<spring:url value="/user/update/${user.id }" var="updateURL" />
					<a href="${updateURL }">Update</a>
				</td>
				<td>
					<spring:url value="/user/delete/${user.id }" var="deleteURL" />
					<a href="${deleteURL }">Delete</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>


</body>
</html>