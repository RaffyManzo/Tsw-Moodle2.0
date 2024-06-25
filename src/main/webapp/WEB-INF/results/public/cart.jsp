<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.beans.Corso" %>
<%@ page import="model.beans.Utenza" %>
<%
    Utenza user = (Utenza) session.getAttribute("User");
    Map<Corso, Integer> cart = (Map<Corso, Integer>) request.getAttribute("cart");
%>
<!DOCTYPE html>
<html>
<head>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/cart.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/delete-margin.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/slider.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/footer.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link crossorigin href="https://fonts.gstatic.com" rel="preconnect">
    <link href="https://fonts.googleapis.com/css2?family=Jura:wght@300..700&display=swap" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/script/imageErrorDetect.js"></script>

    <script src="${pageContext.request.contextPath}/script/headerAccessBehaviour.js"></script>
    <title>Shopping Cart - Learn hub</title>
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
                    <a href="${pageContext.request.contextPath}/dashboard" class="header-redirect-btn"
                       id="header-redirect-to-dashboard">Vai alla tua dashboard</a>

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

                    <% } else { %>
                    <a href="${pageContext.request.contextPath}/registrazione.html" class="header-redirect-btn"
                       id="header-redirect-to-registration">Registrati</a><%}%>
                    <% %>
                </div>
            </div>
        </nav>
    </label>
    <div class="header-links" id="header-links">

        <span class="vertical-separator"></span>
        <div class="link-container header-button dashboard-button">
            <% if (user != null) {
            %>
            <a href="${pageContext.request.contextPath}/dashboard" class="header-redirect-btn"
               id="header-redirect-to-dashboard">Vai alla tua dashboard</a>

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
                <img src="${pageContext.request.contextPath}/file?file=${user.getImg()}&id=${user.getIdUtente()}&c=user"
                     alt="<%= initials %>" id="profile-pic">
                <div class="initials" style="display: none;"><%= initials %>
                </div>
            </a>

            <% } else { %>
            <div class="link-container header-button registration-button">
                <a href="${pageContext.request.contextPath}/registrazione.html" class="header-redirect-btn"
                   id="header-redirect-to-registration">Registrati</a><%}%>
                <% %>

            </div>
        </div>
    </div>
</div>
<div class="container">
    <h1>Il tuo carrello</h1>
    <%  Integer total = 0;
        if (cart != null && !cart.isEmpty())
            for (Map.Entry<Corso, Integer> entry : cart.entrySet())
                total += entry.getValue();
    %>

    <div class="header-cart">
        <label>(<%= total %>) Corsi aggiunti al tuo carrello</label>
        <form action="shop" method="get">
            <input type="hidden" name="action" value="empty"/>
            <input id="reset-cart-btn" type="submit" value="Rimuovi tutto"/>
        </form>
    </div>
    <h6 class="line-divider"></h6>

    <div class="summary-container">
        <div class="cart-list-container">
            <%

                if (cart != null && !cart.isEmpty()) {
                    for (Map.Entry<Corso, Integer> entry : cart.entrySet()) {
                        Corso product = entry.getKey();
                        Integer quantity = entry.getValue();
            %>


            <div class="cart-item">
                <img src="file?file=<%= product.getIdCorso() + "/" + product.getImmagine() %>&c=course" alt="/" class="product-image"/>
                <div class="cart-item-info">
                    <h5><%= product.getNome() %></h5>
                    <p><%= product.getDescrizione() %></p>
                    <div class="cart-item-sub-elements">
                        <div class="quantity-controller">
                            <a href="shop?action=decreaseQuantity&productId=<%= product.getIdCorso() %>"><strong>-</strong></a>
                            <p><%= quantity %></p>
                            <a href="shop?action=addToCart&productId=<%= product.getIdCorso() %>"><strong>+</strong></a>
                        </div>
                        <p><%= product.getPrezzo() %>$</p>
                        <form action="shop" method="get" style="display:inline;">
                            <input type="hidden" name="action" value="removeFromCart"/>
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
            <p id="empty-cart">Your cart is empty.</p>
            <%
                }
            %>

        </div>
        <div class="checkout-container">
            <h4>Checkout</h4>
            <h1><% double price = 0 ; if (cart != null) { for (Map.Entry<Corso, Integer> entry : cart.entrySet()){
                price += entry.getKey().getPrezzo() * entry.getValue();}%> <%}%><%= Math.round(price * 100.0) / 100.0 %> $</h1>
            <a href="${pageContext.request.contextPath}/checkout" class="checkout-btn">
                Completa il pagamento
            </a>
        </div>
    </div>

</div>
<footer class="footer">
    <div class="footer-container">
        <div class="logo">
            <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Logo">
            <label>Learn Hub</label>
        </div>
        <div class="sub-footer-container">
            <ul class="contact-info">
                <li><strong>Indirizzo:</strong> Via Giuseppe Garibaldi 69, Prato (IT)</li>
                <li><strong>Recapiti telefonici:</strong> +39 467 236 7722</li>
                <li><strong>Email:</strong> assistenza@learnhub.com</li>
            </ul>
            <div class="newsletter">
                <label>Iscriviti alla newsletter</label>
                <form class="input-email-box-newsletter" action="${pageContext.request.contextPath}/sub">
                    <input type="email" placeholder="latuamailpersonale@dominio.com">
                    <button type="submit">Iscriviti</button>
                </form>
                <div class="social-icons">
                    <a class="social-icon-box" href="https://www.instagram.com/learn._.hub/">
                        <img src="${pageContext.request.contextPath}/assets/images/instagram%201.png" alt="Ig">
                    </a>
                    <a class="social-icon-box" href="https://www.facebook.com/search/top/?q=learn%20hub">
                        <img src="${pageContext.request.contextPath}/assets/images/facebook%201.png" alt="Fb">
                    </a>
                </div>
            </div>
        </div>
    </div>

</footer>
</body>
</html>