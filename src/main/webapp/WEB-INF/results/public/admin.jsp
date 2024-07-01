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

<form action="AdminServlet">
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
    <ul>
        <%
            if(request.getAttribute("table")!=null){
            switch ((String) request.getAttribute("table")){
                case "utenza":
                    List<Utenza> utenti = (List<Utenza>) request.getAttribute("data");
                    for (Utenza u : utenti) {
        %>
                    <li>
                        id: <%= u.getIdUtente() %><br>
                        <%= u.getCognome() %> <%= u.getNome() %> Tipo: <%= u.getTipo() %><br>
                        email: <%= u.getEmail() %><br>
                        username: <%= u.getUsername() %><br>
                        data creazione account: <%= u.getDataCreazioneAccount() %><br>
                        <div class="button-container">
                            <form action="EliminaServlet">
                                <input type="hidden" name="id" value="<%= u.getIdUtente() %>">
                                <input type="hidden" name="tipo" value="utenza">
                                <input type="submit" value="Elimina">
                            </form>
                            <form action="ModificaServlet">
                                <input type="hidden" name="id" value="<%= u.getIdUtente() %>">
                                <input type="submit" value="Modifica">
                            </form>
                        </div>
                    </li>
        <%
                    }
                    break;
                case "corso":
                    List<Corso> corsi = (List<Corso>) request.getAttribute("data");
                    for (Corso c : corsi) {
        %>
                <li>
                    id: <%= c.getIdCorso() %><br>
                    <%= c.getNome() %> - <%= c.getCreatore().getCognome() %> <%= c.getCreatore().getNome() %><br>
                    Categoria: <%= c.getNomeCategoria() %><br>
                    Descrizione: <%= c.getDescrizione() %><br>
                    Prezzo: <%= c.getPrezzo() %><br>
                    <div class="button-container">
                        <form action="EliminaServlet">
                            <input type="hidden" name="id" value="<%= c.getIdCorso() %>">
                            <input type="hidden" name="tipo" value="corso">
                            <input type="submit" value="Elimina">
                        </form>
                        <form action="ModificaServlet">
                            <input type="hidden" name="id" value="<%= c.getIdCorso() %>">
                            <input type="submit" value="Modifica">
                        </form>
                    </div>
                </li>
        <%
                }
                break;
            case "categoria":
                List<Categoria> categorie = (List<Categoria>) request.getAttribute("data");
                for (Categoria c : categorie) {
        %>
                <li>
                    Nome: <%= c.getNome() %><br>
                    <div class="button-container">
                        <form action="EliminaServlet">
                            <input type="hidden" name="tipo" value="categoria">
                            <input type="hidden" name="id" value="<%= c.getNome() %>">
                            <input type="submit" value="Elimina">
                        </form>
                        <form action="ModificaServlet">
                            <input type="hidden" name="id" value="<%= c.getNome() %>">
                            <input type="submit" value="Modifica">
                        </form>
                    </div>
                </li>
        <%
                }
                break;
                default: System.out.println("Errore");
            }
            }
        %>
    </ul>

</div>

</body>
</html>
