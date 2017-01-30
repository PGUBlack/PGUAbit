<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Spec Adding Form</title>
    <link href="<c:url value='/static/css/bootstrap.css' />" rel="stylesheet"></link>
    <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
</head>

<body>
<div id="mainWrapper">
    <div class="well lead">Main Menu <span class="floatRight"><a href="<c:url value="/logout" />">Logout</a></span> </div>
    <div class="login-container">
        <div class="login-card">
            <div class="login-form">



                <button onclick="window.location.href='<c:url value='/list' />';" class="btn btn-block btn-primary btn-default">Users</button>
                <button onclick="window.location.href='<c:url value='/specs' />';" class="btn btn-block btn-primary btn-default">Specs</button>
                </div>
            </div>
        </div>
</div>

</body>
</html>