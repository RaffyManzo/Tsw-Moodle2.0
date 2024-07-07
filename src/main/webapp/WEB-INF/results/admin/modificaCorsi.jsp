<%@ page import="model.dao.CartDaoImpl" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.dao.LezioneDaoImpl" %>
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
        int idCorso = Integer.parseInt(request.getAttribute("elemento").toString());
        LezioneDaoImpl lDao = new LezioneDaoImpl();
        List<Lezione> lezioni = lDao.findAllByCorsoId(idCorso);

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
                        <button type="submit" value="View" class="view-lezione">
                            Elimina
                        </button>
                    </form>
                </div>
            </td>
        </tr>
        <%
                }
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