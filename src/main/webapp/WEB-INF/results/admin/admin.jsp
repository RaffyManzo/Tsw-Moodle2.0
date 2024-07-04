<%@ page import="model.beans.Utenza" %>
<%@ page import="model.beans.Corso" %>
<%@ page import="java.util.List" %>
<%@ page import="model.beans.Categoria" %>
<%
    //da spostare in privato ma ora è qui per vedere se funziona e non fare il login ogni volta
    Utenza user = (Utenza) request.getSession(false).getAttribute("user");
%>

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
    <!-- <div class="link-container">
        <a href="${pageContext.request.contextPath}/AdminServlet"
           class="header-redirect-btn" >admin </a>
    </div>!-->

    <div class="header-main-info" id="header-main-info">
        <a href="${pageContext.request.contextPath}/home" class="logo-image" id="header-logo-image">
            <img src="${pageContext.request.contextPath}/assets/images/logo.png">
        </a>
        <h1 class="site-name" id="header-site-name">
            Learn Hub
        </h1>

    </div>
    <!-- We add the checkbox -->
    <input type="checkbox" id="hamburger-input" class="burger-shower"/>

    <!--
      We use a `label` element with the `for` attribute
      with the same value as  our label `id` attribute
    -->
    <label id="hamburger-menu" for="hamburger-input">
        <nav id="sidebar-menu">
            <div class="ul">
                <div class="link-container">
                    <% if (user != null) {
                    %>
                    <p class="header-text">Area admin</p>

                    <% } else { %>
                    <a href="${pageContext.request.contextPath}/login.html" class="header-redirect-btn"
                       id="header-redirect-to-login">Login</a> <%}%>
                    <% %>
                </div>
                <div class="link-container">
                    <% if (user != null) {
                    %>
                    <a href="${pageContext.request.contextPath}/account" class="header-redirect-btn"
                       id="header-redirect-to-profile">My account</a>

                    <% } %>
                </div>
                <%if(user != null) {%>
                <div class="link-container">
                    <a href="${pageContext.request.contextPath}/logout" class="header-redirect-btn">
                        Logout
                    </a>
                </div>
                <%}%>
            </div>
        </nav>
    </label>
    <div class="header-links" id="header-links">

        <span class="vertical-separator"></span>
        <div class="link-container">
            <% if (user != null) {
            %>
            <p class="header-text">Area admin</p>

            <% } else { %>
            <a href="${pageContext.request.contextPath}/login.html" class="header-redirect-btn"
               id="header-redirect-to-login">Login</a> <%}%>
            <% %>

        </div>
        <span class="vertical-separator"></span>
        <% if (user != null) {
            String initials = "";
            if (user.getNome() != null && user.getCognome() != null) {
                initials = user.getNome().charAt(0) + "" + user.getCognome().charAt(0);
            }

        %>
        <div class="link-container header-button account-button">

            <a href="${pageContext.request.contextPath}/account" class="header-redirect-btn"
               id="header-redirect-to-profile">
                <img src="${pageContext.request.contextPath}/file?file=${user.getImg()}&id=${user.getIdUtente()}&c=user" alt="<%= initials %>" id="profile-pic">
                <div class="initials" style="display: none;"><%= initials %></div></a>

            <% } else { %>
            <div class="link-container header-button registration-button">
                <a href="${pageContext.request.contextPath}/registrazione.html" class="header-redirect-btn"
                   id="header-redirect-to-registration">Registrati</a><%}%>
                <% %>

            </div>
            <span class="vertical-separator"></span>
            <%if(user != null) {%>
            <span class="vertical-separator"></span>
            <div class="link-container">
                <a href="${pageContext.request.contextPath}/logout" class="header-redirect-btn">
                    Logout&nbsp;&nbsp;<img src="${pageContext.request.contextPath}/assets/images/log-out.png" alt="">
                </a>
            </div>
            <%}%>
        </div>

    </div>

<form action="admin">
    <div class="controls" >
        <select id="table-select" name="table-select">
            <option value="utenza" <%= request.getAttribute("table") != null && request.getAttribute("table").equals("utenza") ? "selected" : "" %>>Utenza</option>
            <option value="corso" <%= request.getAttribute("table") != null && request.getAttribute("table").equals("corso") ? "selected" : "" %>>Corso</option>
            <option value="categoria" <%= request.getAttribute("table") != null && request.getAttribute("table").equals("categoria") ? "selected" : "" %>>Categoria</option>
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
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Cognome</th>
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
            <td><%= u.getTipo() %></td>
            <td><%= u.getEmail() %></td>
            <td><%= u.getUsername() %></td>
            <td><%= u.getDataCreazioneAccount() %></td>
            <td>
                <div class="button-container">
                    <form action="adminDelete" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="<%= u.getIdUtente() %>">
                        <input type="hidden" name="tipo" value="utenza">
                        <input type="submit" value="Elimina">
                    </form>
                    <form action="ModificaServlet" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="<%= u.getIdUtente() %>">
                        <input type="hidden" name="tipo" value="utenza">
                        <input type="submit" value="Modifica">
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
        case "corso":
            List<Corso> corsi = (List<Corso>) request.getAttribute("data");
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
            <td>
                <div class="button-container">
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
    <table>
        <thead>
        <tr>
            <th>Nome</th>
            <th>Azioni</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Categoria c : categorie) {
        %>
        <tr>
            <td><%= c.getNome() %></td>
            <td>
                <div class="button-container">
                    <form action="adminDelete" method="post" style="display:inline;">
                        <input type="hidden" name="tipo" value="categoria">
                        <input type="hidden" name="id" value="<%= c.getNome() %>">
                        <input type="submit" value="Elimina">
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
