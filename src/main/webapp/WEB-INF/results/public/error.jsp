<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: raffa
  Date: 17/06/2024
  Time: 16:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link crossorigin href="https://fonts.gstatic.com" rel="preconnect">
    <link href="https://fonts.googleapis.com/css2?family=Jura:wght@300..700&display=swap" rel="stylesheet">
    <meta charset="UTF-8">
    <title>Error - Learn Hub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/error.css">
</head>
<body>
<header class="header">
    <label>ERRORE: qualcosa è andato storto</label>
</header>
<div class="container">
    <div class="error-box">
        <img src="${pageContext.request.contextPath}/assets/images/error.jpg" alt="Error Image">
        <div class="error-list">
            <label>Ti spiego perché:</label>
            <ul>
                <%
                    ArrayList<String> errors = (ArrayList<String>) request.getAttribute("errors");
                    if (errors != null)
                        for (String err : errors) {%>
                <%= "<li>" + err + "</li>"%>
                <%}%>
            </ul>
        </div>
    </div>
    <div class="buttons">
        <a href="${pageContext.request.contextPath}/login.html" class="button">Vai alla pagina di login</a>
        <a href="${pageContext.request.contextPath}/home" class="button">Vai alla Home senza login</a>
    </div>
</div>

</body>
</html>