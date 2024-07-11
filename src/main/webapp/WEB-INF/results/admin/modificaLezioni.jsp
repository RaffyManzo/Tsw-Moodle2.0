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
        List<Lezione> lezioni = (List<Lezione>) request.getAttribute("elemento");
        int idCorso=Integer.parseInt(request.getAttribute("corso").toString());

        if (lezioni != null && !lezioni.isEmpty()) {
    %>

    <table>
        <thead>
        <tr>
            <th>Titolo</th>
            <th>Descrizione</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Lezione lezione : lezioni) {
                String titolo = lezione.getTitolo();
                String descrizione = lezione.getDescrizione();
                int idLezione=lezione.getId();
        %>
        <tr>
            <td><%= titolo %></td>
            <td><%=descrizione%></td>
            <td>
                <div class="button-container">
                    <form action="VisualizzaLezioni">
                        <input type="hidden" name="idLezione" value="<%= idLezione %>"/>
                        <input type="hidden" name="idCorsoV" value="<%= idCorso %>"/>
                        <button type="submit" value="View" class="view-lezione">Elimina </button>
                    </form>
                    <form action="VisualizzaArgomenti">
                        <input type="hidden" name="type" value="view"/>
                        <input type="hidden" name="idCorsoV" value="<%= idCorso %>"/>
                        <input type="hidden" name="idLezione" value="<%= idLezione %>"/>
                        <input type="submit" value="Visualizza argomenti della lezione">
                    </form>
                </div>
            </td>
        </tr>
        <%
                }
            }else{
        %>
            <p>Non ci sono lezioni per questo corso</p>
        <%
            }

        %>
        </tbody>
    </table>
    <form action="ModificaServlet">
        <input type="hidden" name="tipo" value="corso">
        <input type="hidden" name="id" value="<%= idCorso %>">
        <input type="submit" value="Indietro">
    </form>
</div>
</body>
</html>