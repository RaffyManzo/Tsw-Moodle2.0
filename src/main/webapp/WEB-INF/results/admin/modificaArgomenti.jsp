<%@ page import="java.util.List" %>
<%@ page import="model.beans.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Learn Hub - Area admin</title>
    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/admin.css" rel="stylesheet">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/logo.ico" >
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
        List<Argomento> argomenti=(List<Argomento>) request.getAttribute("elemento");

        if (argomenti != null && !argomenti.isEmpty()) {
    %>
    <table>
        <thead>
        <tr>
            <th>Id</th>
            <th>Nome</th>
            <th>Descrizione</th>
            <th>Data Caricamento</th>
            <th>Azione</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Argomento argomento : argomenti) {
                String nome = argomento.getNome();
                String descrizione = argomento.getDescrizione();
                String dataCaricamento = argomento.getDataCaricamento().toString();
                int idArgomento = argomento.getId();
        %>
        <tr>
            <td><%= idArgomento %></td>
            <td><%= nome %></td>
            <td><%= descrizione %></td>
            <td><%= dataCaricamento %></td>
            <td>
                <div class="button-container">
                    <form action="VisualizzaArgomenti" method="post">
                        <input type="hidden" name="idArgomento" value="<%= idArgomento %>"/>
                        <input type="hidden" name="idCorsoV" value="<%= Integer.parseInt(request.getAttribute("corso").toString()) %>">
                        <input type="hidden" name="idLezione" value="<%= Integer.parseInt(request.getAttribute("idLezione").toString())  %>"/>
                        <input type="hidden" name="type" value="delete"/>
                        <button type="submit" value="Elimina" class="elimina-argomento">Elimina</button>
                    </form>
                    <form action="VisualizzaFile" method="get">
                        <input type="hidden" name="idCorsoV" value="<%= Integer.parseInt(request.getAttribute("corso").toString()) %>">
                        <input type="hidden" name="idArgomento" value="<%= idArgomento %>"/>
                        <input type="hidden" name="idLezione" value="<%= Integer.parseInt(request.getAttribute("idLezione").toString()) %>"/>
                        <input type="submit" value="Visualizza file">
                    </form>
                </div>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <p>Non ci sono argomenti per questa lezione</p>
        <%
            }
        %>
        </tbody>
    </table>
    <form action="VisualizzaLezioni">
        <input type="hidden" name="idCorsoV" value="<%= Integer.parseInt(request.getAttribute("corso").toString()) %>">
        <input type="submit" value="Indietro">
    </form>
</div>
</body>
</html>