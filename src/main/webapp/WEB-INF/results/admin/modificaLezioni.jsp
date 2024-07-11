<%@ page import="model.beans.Lezione" %>

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
<div class="form-container">
    <%
        Lezione l = (Lezione) request.getAttribute("lezione");
    %>
    <form action="ModificaLezioni">
        <div class="form-group">
            <label for="titolo">Titolo:</label>
            <input type="text" id="titolo" name="titolo" value="<%= l.getTitolo() %>">
        </div>
        <div class="form-group">
            <label for="descrizione">Descrizione:</label>
            <input type="text" id="descrizione" name="descrizione" value="<%= l.getDescrizione() %>">
        </div>
        <input type="hidden" name="idLezione" value="<%= l.getId() %>"/>
        <input type="hidden" name="idCorsoV" value="<%= Integer.parseInt(request.getAttribute("corso").toString()) %>"/>
        <input type="hidden" name="type" value="modify"/>
        <div class="button-container">
            <input type="submit" value="Salva Modifiche">
            <form action="VisualizzaLezioni">
                <input type="hidden" name="idCorsoV" value="<%= Integer.parseInt(request.getAttribute("corso").toString()) %>">
                <input type="submit" value="Indietro">
            </form>
        </div>
    </form>
</div>
</body>
</html>