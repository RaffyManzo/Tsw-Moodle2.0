<%@ page import="model.beans.Corso" %>
<%@ include file="../private/session.jsp" %>

<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Learn Hub - Area admin</title>
    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/admin.css" rel="stylesheet">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/logo.ico">
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
        List<Corso> corsi = (List<Corso>) request.getAttribute("data");
        if(corsi!=null){
            %>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Creatore</th>
            <th>Categoria</th>
            <th>Descrizione</th>
            <th>Prezzo</th>
        </tr>
        </thead>
        <tbody>
    <%
        for (Corso c : corsi) {
    %>
    <tr>
        <td><%= c.getIdCorso() %></td>
        <td><%= c.getNome() %></td>
        <td><%= c.getCreatore().getCognome() %> <%= c.getCreatore().getNome() %></td>
        <td><%= c.getNomeCategoria() %></td>
        <td><%= c.getDescrizione() %></td>
        <td><%= c.getPrezzo() %></td>
    </tr>
    <%
        }
        }
    %>
    </tbody>
    </table>
    <form action="admin">
        <input type="hidden" name="table-select" value="ordine">
        <input type="submit" value="Indietro">
    </form>
</div>
</body>
</html>