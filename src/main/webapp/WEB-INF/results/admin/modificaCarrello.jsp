<%@ page import="model.beans.Utenza" %>
<%@ page import="model.beans.Corso" %>
<%@ page import="model.dao.CartDaoImpl" %>
<%@ page import="model.beans.Carrello" %>
<%@ page import="java.util.Map" %>
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
        <h1 class="admin-header">Area admin</h1>
    </div>
</div>

<div id="table-container">
    <%
        int idUtente = Integer.parseInt(request.getAttribute("elemento").toString());
        CartDaoImpl c=new CartDaoImpl();
        Carrello carrello=c.getCartByUserID(idUtente);
        Map<Corso, Integer> cart =carrello.getCart();
        if (cart != null && !cart.isEmpty()) {
            for (Map.Entry<Corso, Integer> entry : cart.entrySet()) {
                Corso product = entry.getKey();
                Integer quantity = entry.getValue();
                double totale=product.getPrezzo()*quantity;
    %>
        <div class="cart-item">
            <div class="cart-item-info">
                <h5><%= product.getNome() %></h5>
                <p><%= product.getDescrizione() %></p>
                <div class="cart-item-sub-elements">
                    <p><%= product.getPrezzo() %>$</p>
                    <p>Quantità: <%= quantity %></p>
                    <p>Totale: <%= totale %>$</p>
                    <form action="shop" method="get" style="display:inline;">
                        <input type="hidden" name="action" value="removeFromCart"/>
                        <input type="hidden" name="id" value="<%= idUtente %>"/>
                        <input type="hidden" name="admin" value="true"/>
                        <input type="hidden" name="productId" value="<%= product.getIdCorso() %>"/>
                        <button type="submit" value="Remove" class="trash-cart-item">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash" viewBox="0 0 16 16">
                                <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0z"></path>
                                <path d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4zM2.5 3h11V2h-11z"></path>
                            </svg>
                        </button>
                    </form>
                </div>
            </div>
        </div>
            <% }} else {
            %>
        <p id="empty-cart">Il carrello è vuoto</p>
            <%
                }
            %>
        <div class="button-container">
            <button type="button" onclick="window.history.back();">Indietro</button>
        </div>
</body>
</html>