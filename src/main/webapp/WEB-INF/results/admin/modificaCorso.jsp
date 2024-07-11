<%@ page import="model.beans.Corso" %>
<%@ page import="model.dao.CorsoDaoImpl" %>

<%
    Corso c = (Corso) request.getAttribute("elemento");
%>
<div class="form-container">
    <form action="ModificaServlet" method="post">
        <div class="form-group">
            <label for="idCorso">ID:</label>
            <input type="text" id="idCorso" name="id" value="<%= c.getIdCorso() %>" readonly>
        </div>
        <div class="form-group">
            <label for="nomec">Nome:</label>
            <input type="text" id="nomec" name="nome" value="<%= c.getNome() %>">
        </div>
        <div class="form-group">
            <label for="creatore">Id creatore:</label>
            <input type="text" id="creatore" name="creatore" value="<%= c.getCreatore().getIdUtente() %>">
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
        <div class="form-group">
            <label for="certificazione">Certificazione:</label>
            <input type="text" id="certificazione" name="certificazione" value="<%= c.getCertificazione() %>">
        </div>
        <div class="form-group">
            <label for="creazione">Creazione:</label>
            <input type="date" id="creazione" name="creazione" value="<%= c.getDataCreazione() %>">
        </div>
        <div class="form-group">
            <label for="acquisti">Numero di acquisti:</label>
            <input type="number" id="acquisti" name="acquisti" value="<%= c.getNumeroAcquisti() %>" readonly>
        </div>
        <div class="form-group">
            <label for="immagine">Immagine:</label>
            <input name="immagine" id="immagine" value="<%= c.getImmagine() %>">
        </div>
        <input type="hidden" name="tipo" value="corso">
        <div class="button-container">
            <input type="submit" value="Salva Modifiche">
            <form action="admin">
                <input type="hidden" name="table-select" value="corso">
                <input type="submit" value="Indietro">
            </form>
        </div>
    </form>
    <form action="VisualizzaLezioni">
        <input type="hidden" name="idCorsoV" value="<%= c.getIdCorso() %>">
        <input type="submit" value="Visualizza lezioni del corso">
    </form>
</div>
