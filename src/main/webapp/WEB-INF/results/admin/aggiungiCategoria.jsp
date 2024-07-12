<div class="form-container">
    <form action="ModificaServlet" method="post">
        <div class="form-group">
            <label for="nome">Nome:</label>
            <input type="text" id="nome" name="nome">
        </div>
        <input type="hidden" name="tipo" value="categoria">
        <div class="button-container">
            <input type="submit" value="Aggiungi">
            <form action="admin">
                <input type="hidden" name="table-select" value="categoria">
                <input type="submit" value="Indietro">
            </form>
        </div>
    </form>
</div>
