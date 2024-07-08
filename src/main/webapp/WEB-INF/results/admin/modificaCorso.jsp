<%@ page import="model.beans.Corso" %>
<%@ page import="model.dao.CorsoDaoImpl" %>

<%
    CorsoDaoImpl corsoDao = new CorsoDaoImpl();
    Corso c = corsoDao.findByID(Integer.parseInt(request.getAttribute("elemento").toString()));
%>
<div class="form-container">
    <form action="ModificaServlet" method="post">
        <div class="form-group">
            <label for="idCorso">ID:</label>
            <input type="text" id="idCorso" name="id" value="<%= c.getIdCorso() %>" readonly>
        </div>
        <!-- Altri campi di input qui -->
        <div class="button-container">
            <input type="submit" value="Salva Modifiche">
            <form action="admin">
                <input type="hidden" name="table-select" value="corso">
                <input type="submit" value="Indietro">
            </form>
        </div>
    </form>
    <form action="VisualizzaLezioni">
        <input type="hidden" name="tipo" value="corso">
        <input type="hidden" name="idCorsoV" value="<%= c.getIdCorso() %>">
        <input type="submit" value="Visualizza lezioni del corso">
    </form>
</div>
