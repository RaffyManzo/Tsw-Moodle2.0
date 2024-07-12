<%@ page import="model.beans.Utenza" %>
<%@ page import="model.beans.Corso" %>
<%@ page import="java.util.List" %>
<%@ include file="../private/session.jsp" %>

<%@ page import="model.beans.Categoria" %>
<%@ page import="model.beans.Ordine" %>

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
    </div>
    <span class="vertical-separator"></span>
    <div class="link-container">
        <a href="${pageContext.request.contextPath}/logout" class="header-redirect-btn">
            Logout&nbsp;&nbsp;<img src="${pageContext.request.contextPath}/assets/images/log-out.png" alt="">
        </a>
    </div>
</div>

<form action="admin">
    <div class="controls" >
        <select id="table-select" name="table-select">
            <option value="utenza" <%= request.getAttribute("table") != null && request.getAttribute("table").equals("utenza") ? "selected" : "" %>>Utenza</option>
            <option value="corso" <%= request.getAttribute("table") != null && request.getAttribute("table").equals("corso") ? "selected" : "" %>>Corso</option>
            <option value="categoria" <%= request.getAttribute("table") != null && request.getAttribute("table").equals("categoria") ? "selected" : "" %>>Categoria</option>
            <option value="ordine" <%= request.getAttribute("table") != null && request.getAttribute("table").equals("ordine") ? "selected" : "" %>>Ordine</option>
        </select>
        <input id="load-table" type="submit" value="Carica dati"/>
    </div>
</form>

<div id="table-container">
    <%
        if (request.getAttribute("table") != null) {
            switch ((String) request.getAttribute("table")) {
                case "utenza":
                    List<Utenza> utenti = (List<Utenza>) request.getAttribute("data");
    %>

    <form action="adminSearchFilter">
        <input type="hidden" name="table" value="utenza">
        <div class="filter-container">
            <div class="form-group">
                <label for="emailUtente">Email:</label>
                <input type="text" id="emailUtente" name="emailUtente">
                <label for="tipoUtente">Tipo:</label>
                <input type="text" id="tipoUtente" name="tipoUtente" maxlength="1" pattern="[DAS]">
                <input type="submit" value="Filtra">
            </div>
        </div>
    </form>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Cognome</th>
            <th>Nazione</th>
            <th>Tipo</th>
            <th>Email</th>
            <th>Username</th>
            <th>Data Creazione Account</th>
            <th>Azioni</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Utenza u : utenti) {
        %>

        <tr>
            <td><%= u.getIdUtente() %></td>
            <td><%= u.getNome() %></td>
            <td><%= u.getCognome() %></td>
            <td><%= u.getNazione() %></td>
            <td><%= u.getTipo() %></td>
            <td><%= u.getEmail() %></td>
            <td><%= u.getUsername() %></td>
            <td><%= u.getDataCreazioneAccount() %></td>
            <td>
                <%if(u.getIdUtente() != 0) {%>
                    <div class="button-container">
                        <form action="adminDelete" method="post" style="display:inline;">
                            <input type="hidden" name="id" value="<%= u.getIdUtente() %>">
                            <input type="hidden" name="tipo" value="utenza">
                            <input type="hidden" name="nome" value="null">
                            <input type="submit" value="Elimina">
                        </form>
                        <form action="ModificaServlet" method="post" style="display:inline;">
                            <input type="hidden" name="id" value="<%= u.getIdUtente() %>">
                            <input type="hidden" name="tipo" value="utenza">
                            <input type="submit" value="Modifica">
                        </form>
                    </div>
                <%}%>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
    <%
            break;
        case "corso":
            List<Corso> corsi = (List<Corso>) request.getAttribute("data");
    %>

    <form action="adminSearchFilter">
        <input type="hidden" name="table" value="corso">
        <div class="filter-container">
            <label for="nomeCorso">Nome corso:</label>
            <input type="text" id="nomeCorso" name="nomeCorso">
            <label for="nomeCategoria">Nome categoria:</label>
            <input type="text" id="nomeCategoria" name="nomeCategoria">
            <input type="submit" value="Filtra">
        </div>
    </form>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Creatore</th>
            <th>Categoria</th>
            <th>Descrizione</th>
            <th>Prezzo</th>
            <th>Eliminato</th>
            <th>Azioni</th>
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
            <td><%= c.isDeleted() %></td>
            <td>
                <div class="button-container">
                    <%if(!c.isDeleted()) {%>
                        <form action="adminDelete" method="post" style="display:inline;">
                            <input type="hidden" name="id" value="<%= c.getIdCorso() %>">
                            <input type="hidden" name="tipo" value="corso">
                            <input type="submit" value="Elimina">
                        </form>
                        <form action="ModificaServlet" method="post" style="display:inline;">
                            <input type="hidden" name="id" value="<%= c.getIdCorso() %>">
                            <input type="hidden" name="tipo" value="corso">
                            <input type="submit" value="Modifica">
                        </form>
                    <%}else{%>
                        <form action="adminDelete" method="post" style="display:inline;">
                            <input type="hidden" name="id" value="<%= c.getIdCorso() %>">
                            <input type="hidden" name="tipo" value="corso">
                            <input type="submit" value="Ripristina">
                        </form>
                    <%}%>
                </div>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
    <%
            break;
        case "categoria":
            List<Categoria> categorie = (List<Categoria>) request.getAttribute("data");
    %>

    <form action="adminSearchFilter">
        <input type="hidden" name="table" value="categoria">
        <div class="filter-container">
            <label for="nomeCat">Nome categoria:</label>
            <input type="text" id="nomeCat" name="nomeCat">
            <input type="submit" value="Filtra">
        </div>
    </form>
    <form action="ModificaServlet" method="post" style="display:inline;">
        <input type="hidden" name="tipo" value="categoria">
        <input type="submit" value="Nuova categoria">
    </form>

    <table>
        <thead>
        <tr>
            <th>Nome</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Categoria c : categorie) {
        %>
        <tr>
            <td><%= c.getNome() %></td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
    <%
            break;
        case "ordine":
            List<Ordine> ordini = (List<Ordine>) request.getAttribute("data");
    %>

    <form action="adminSearchFilter">
        <input type="hidden" name="table" value="ordine">
        <div class="filter-container">
            <label for="IDUtente">Id utente:</label>
            <input type="number" id="IDUtente" name="IDUtente">
            <label for="IDordine">Id ordine:</label>
            <input type="number" id="IDordine" name="IDordine">
            <input type="submit" value="Filtra">
        </div>
    </form>

    <table>
        <thead>
        <tr>
            <th>ID Ordine</th>
            <th>Numero Carta</th>
            <th>ID Utente</th>
            <th>Data Pagamento</th>
            <th>Totale</th>
            <th>Timestamp</th>
            <th>UUID</th>
            <th>Azioni</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Ordine o : ordini) {
        %>
        <tr>
            <td><%= o.getId() %></td>
            <td><%= o.getNumeroCarta() %></td>
            <td><%= o.getIdutente() %></td>
            <td><%= o.getDataPagamento() %></td>
            <td><%= o.getTotale() %></td>
            <td><%= o.getTimestamp() %></td>
            <td><%= o.getUuid() %></td>
            <td>
                <div class="button-container">
                    <form action="VisualizzaOrdine">
                        <input type="hidden" name="id" value="<%= o.getId() %>">
                        <input type="submit" value="Dettagli">
                    </form>
                </div>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
    <%
                    break;
                default:
                    System.out.println("Errore");
            }
        }
    %>
</div>

</body>
</html>
