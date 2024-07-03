<%@ page import="model.beans.Utenza" %>
<%@ page import="model.beans.Corso" %>
<%@ page import="java.util.List" %>
<%@ page import="model.beans.Categoria" %>
<%@ page import="java.util.Objects" %>
<%@ page import="model.dao.UtenzaDaoImpl" %>
<%@ page import="model.dao.CorsoDaoImpl" %>
<%@ page import="model.dao.CategoriaDaoImpl" %>
<%@ page import="java.util.ArrayList" %>
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
        <a href="${pageContext.request.contextPath}/home" class="logo-image" id="header-logo-image">
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
                UtenzaDaoImpl utenza = new UtenzaDaoImpl();
                Utenza u = utenza.findByID(Integer.parseInt(request.getAttribute("elemento").toString()));
    %>
    <div class="form-container">
        <form action="ModificaServlet">
            <div class="form-group">
                <label for="idUtente">ID:</label>
                <input type="text" id="idUtente" name="idUtente" value="<%= u.getIdUtente() %>" readonly>
            </div>
            <div class="form-group">
                <label for="cognome">Cognome:</label>
                <input type="text" id="cognome" name="cognome" value="<%= u.getCognome() %>">
            </div>
            <div class="form-group">
                <label for="nome">Nome:</label>
                <input type="text" id="nome" name="nome" value="<%= u.getNome() %>">
            </div>
            <div class="form-group">
                <label for="tipo">Tipo:</label>
                <input type="text" id="tipo" name="tipo" value="<%= u.getTipo() %>">
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="<%= u.getEmail() %>">
            </div>
            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" value="<%= u.getUsername() %>">
            </div>
            <div class="form-group">
                <label for="dataCreazioneAccount">Data Creazione Account:</label>
                <input type="text" id="dataCreazioneAccount" name="dataCreazioneAccount" value="<%= u.getDataCreazioneAccount() %>" readonly>
            </div>
            <input type="hidden" name="tipo" value="utenza">
            <div class="button-container">
                <input type="submit" value="Salva Modifiche">
                <form action="VisualizzaCarrelloServlet" method="get">
                    <input type="hidden" name="idUtente" value="<%= u.getIdUtente() %>">
                    <input type="submit" value="Visualizza il carrello dell'utente">
                </form>
            </div>
        </form>
    </div>
    <%
            break;
        case "corso":
            CorsoDaoImpl corso = new CorsoDaoImpl();
            Corso c = corso.findByID(Integer.parseInt(request.getAttribute("elemento").toString()));
    %>
    <div class="form-container">
        <form action="ModificaServlet" method="post">
            <div class="form-group">
                <label for="idCorso">ID:</label>
                <input type="text" id="idCorso" name="idCorso" value="<%= c.getIdCorso() %>" readonly>
            </div>
            <div class="form-group">
                <label for="nomec">Nome:</label>
                <input type="text" id="nomec" name="nome" value="<%= c.getNome() %>">
            </div>
            <div class="form-group">
                <label for="creatore">Creatore:</label>
                <input type="text" id="creatore" name="creatore" value="<%= c.getCreatore().getCognome() %> <%= c.getCreatore().getNome() %>">
            </div>
            <div class="form-group">
                <label for="categoria">Categoria:</label>
                <input type="text" id="categoria" name="categoria" value="<%= c.getNomeCategoria() %>">
            </div>
            <div class="form-group">
                <label for="descrizione">Descrizione:</label>
                <input type="text" id="descrizione" name="descrizione" value="<%= c.getDescrizione() %>">
            </div>
            <div class="form-group">
                <label for="prezzo">Prezzo:</label>
                <input type="number" id="prezzo" name="prezzo" value="<%= c.getPrezzo() %>">
            </div>
            <input type="hidden" name="tipo" value="corso">
            <div class="button-container">
                <input type="submit" value="Salva Modifiche">
            </div>
        </form>
    </div>
    <%
            break;
        case "categoria":
            CategoriaDaoImpl categoria = new CategoriaDaoImpl();
            List<Categoria> a=categoria.findByNome(request.getAttribute("elemento").toString());
            Categoria cat = a.getFirst();
    %>
    <div class="form-container">
        <form action="ModificaServlet" method="post">
            <div class="form-group">
                <label for="nomecat">Nome:</label>
                <input type="text" id="nomecat" name="nome" value="<%= cat.getNome() %>">
            </div>
            <input type="hidden" name="tipo" value="categoria">
            <div class="button-container">
                <input type="submit" value="Salva Modifiche">
            </div>
        </form>
    </div>
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
