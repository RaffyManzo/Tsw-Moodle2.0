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
    <title>Success - Learn Hub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/error.css">
</head>
<body>
<header class="header">
    <label style="color: #238636">Operazione di <%= request.getAttribute("operation")%> avvenuta con successo</label>
</header>
<div class="container">
    <div class="error-box" style="background-color: #9bfab0; border-color: #238636">
        <img src="${pageContext.request.contextPath}/assets/images/success.jpg" alt="Error Image">
        <div class="error-list">
            <label><%= request.getAttribute("message-header")%></label>
            <ul>

                <%
                    ArrayList<String> messages = (ArrayList<String>) request.getAttribute("messages");
                    if (messages != null)
                        for (String msg : messages) {%>
                <%= "<li>" + msg + "</li>"%>
                <%}%>
            </ul>
        </div>
    </div>
    <div class="buttons">
        <%if(request.getHeader("Referer").contains("checkout") && request.getSession(false).getAttribute("user") != null) {%>
        <a href="${pageContext.request.contextPath}/dashboard" class="button">Vai alla dashboard</a>
        <%} else {%>
        <%if(request.getHeader("Referer").equals("registrazione.html")) {%>
        <a href="${pageContext.request.contextPath}/login.html" class="button">Vai alla pagina di login</a>
        <%} else {%>
        <a href="${pageContext.request.contextPath}/registrazione.html" class="button">Crea un nuovo account</a>
        <%}%>
        <a href="${pageContext.request.contextPath}/home" class="button">Vai alla Home senza login</a>
        <%}%>
    </div>
</div>

</body>
</html>
