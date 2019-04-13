<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>User</title>
</head>
<body>

<spring:url value="/user/save" var="saveURL" />
<form:form action="${saveURL }" modelAttribute="userForm" method ="POST" >
<form:hidden path="id" />
	<label>Name: </label>
	<form:input path="name" /><br/>
	<button type="submit" >Save</button>
</form:form>

</body>
</html>