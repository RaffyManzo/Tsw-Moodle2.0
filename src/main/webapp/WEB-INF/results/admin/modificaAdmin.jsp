<%@ page import="model.beans.Utenza" %>
<%@ page import="model.beans.Corso" %>
<%@ page import="model.dao.UtenzaDaoImpl" %>
<%@ page import="model.dao.CorsoDaoImpl" %>
<%@ include file="../private/session.jsp" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Learn Hub - Area admin</title>
    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/admin.css" rel="stylesheet">
</head>
<body>
<div class="header" id="header">
    <div class="header-main-info" id="header-main-info">
        <a href="${pageContext.request.contextPath}/admin" class="logo-image" id="header-logo-image">
            <img src="${pageContext.request.contextPath}/assets/images/logo.png">
        </a>
        <h1 class="site-name" id="header-site-name">
            Learn Hub
        </h1>
        <h1 class="admin-header">Area admin</h1>
    </div>
</div>

<div id="table-container">
    <%
        String tipo = (String) request.getAttribute("tipo");
        switch (tipo) {
            case "utenza":
    %>
    <jsp:include page="modificaUtenza.jsp"></jsp:include>
    <%
            break;
        case "corso":
    %>
    <jsp:include page="modificaCorso.jsp"></jsp:include>
    <%
                break;
            default:
                System.out.println("Errore");
                break;
        }
    %>
</div>

</body>
</html>
