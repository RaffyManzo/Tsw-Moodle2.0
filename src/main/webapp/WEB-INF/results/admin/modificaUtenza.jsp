
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
        <div class="form-group">
            <label for="cognome">Cognome:</label>
            <input type="text" id="cognome" name="cognome" value="<%= u.getCognome() %>">
        </div>
        <div class="form-group">
            <label for="nome">Nome:</label>
            <input type="text" id="nome" name="nome" value="<%= u.getNome() %>">
        </div>
        <div class="form-group">
            <label for="tipoUtente">Tipo:</label>
            <input type="text" id="tipoUtente" name="tipoUtente" value="<%= u.getTipo() %>">
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
        <div class="form-group">
            <label for="dataNascita">Data di Nascita:</label>
            <input type="date" id="dataNascita" name="dataNascita" value="<%= u.getDataNascita() %>">
        </div>
        <div class="form-group">
            <label for="indirizzo">Indirizzo:</label>
            <input type="text" id="indirizzo" name="indirizzo" value="<%= u.getIndirizzo() %>">
        </div>
        <div class="form-group">
            <label for="citta">Città:</label>
            <input type="text" id="citta" name="citta" value="<%= u.getCitta() %>">
        </div>
        <div class="form-group">
            <label for="telefono">Telefono:</label>
            <input type="tel" id="telefono" name="telefono" value="<%= u.getTelefono() %>">
        </div>
        <div class="form-group">
            <label for="immagineu">Immagine:</label>
            <input name="immagine" id="immagineu" value="<%= u.getImg() %>">
        </div>
        <input type="hidden" id="password" name="password" value="<%= u.getPassword() %>">
        <input type="hidden" name="tipo" value="utenza">
        <input type="hidden" name="immagine" value="<%= u.getImg() %>">
        <div class="button-container">
            <input type="submit" value="Salva Modifiche">
            <form action="VisualizzaCarrelloServlet" method="get">
                <input type="hidden" name="idUtente" value="<%= u.getIdUtente() %>">
                <input type="submit" value="Visualizza il carrello dell'utente">
            </form>
            <button type="button" onclick="window.history.back();">Indietro</button>
        </div>
    </form>
    <%
        if(u.getTipo().equals("S")){
    %>
    <form action="VisualizzaCarrelloServlet">
        <input type="hidden" name="tipo" value="utenza">
        <input type="hidden" name="id" value="<%= u.getIdUtente() %>">
        <input type="submit" value="Visualizza il carrello dell'utente">
    </form>
    <%
        }
    %>
</div>