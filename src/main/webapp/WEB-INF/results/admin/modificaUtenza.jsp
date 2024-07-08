
<%@ page import="model.beans.Utenza" %>
<%@ page import="model.dao.UtenzaDaoImpl" %>

<%
    UtenzaDaoImpl utenzaDao = new UtenzaDaoImpl();
    Utenza u = utenzaDao.findByID(Integer.parseInt(request.getAttribute("elemento").toString()));
%>
<div class="form-container">
    <form action="ModificaServlet">
        <div class="form-group">
            <label for="idUtente">ID:</label>
            <input type="text" id="idUtente" name="id" value="<%= u.getIdUtente() %>" readonly>
        </div>
        <!-- Altri campi di input qui -->
        <div class="button-container">
            <input type="submit" value="Salva Modifiche">
            <form action="admin">
                <input type="hidden" name="table-select" value="utenza">
                <input type="submit" value="Indietro">
            </form>
        </div>
    </form>
    <% if(u.getTipo().equals("S")) { %>
    <form action="VisualizzaCarrelloServlet">
        <input type="hidden" name="tipo" value="utenza">
        <input type="hidden" name="id" value="<%= u.getIdUtente() %>">
        <input type="submit" value="Visualizza il carrello dell'utente">
    </form>
    <% } %>
</div>
