<%@ page import="model.beans.Categoria" %>

<%
    Categoria c = (Categoria) request.getAttribute("elemento");
%>
<div class="form-container">
    <form action="ModificaServlet" method="post">
        <div class="form-group">
            <label for="nome">Nome:</label>
            <input type="text" id="nome" name="nome" value="<%= c.getNome() %>">
        </div>
        <input type="hidden" name="id" value="<%= c.getNome() %>">
        <input type="hidden" name="tipo" value="categoria">
        <div class="button-container">
            <input type="submit" value="Salva Modifiche">
            <form action="admin">
                <input type="hidden" name="table-select" value="categoria">
                <input type="submit" value="Indietro">
            </form>
        </div>
    </form>
</div>
