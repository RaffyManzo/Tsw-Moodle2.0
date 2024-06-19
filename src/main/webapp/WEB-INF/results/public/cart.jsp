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
    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/cart.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/delete-margin.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/slider.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/footer.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link crossorigin href="https://fonts.gstatic.com" rel="preconnect">
    <link href="https://fonts.googleapis.com/css2?family=Jura:wght@300..700&display=swap" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/script/imageErrorDetect.js" defer></script>

    <script src="${pageContext.request.contextPath}/script/headerAccessBehaviour.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
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
                <div class="link-container">
                    <a href="${pageContext.request.contextPath}/shop?action=viewCart"
                       class="header-redirect-btn" id="li-header-redirect-to-cart">Carrello</a>
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
            <span class="vertical-separator" id="vertical-bar-tofix"></span>

            <div class="link-container">
                <a href="${pageContext.request.contextPath}/shop?action=viewCart" class="header-redirect-btn"
                   id="header-redirect-to-cart"><img
                        src="${pageContext.request.contextPath}/assets/images/shopping-basket.png" alt=""></a>
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
                            <a href="shop?action=decreaseQuantity&productId=<%= product.getIdCorso() %>"><strong>+</strong></a>
                            <p>1</p>
                            <a href="shop?action=decreaseQuantity&productId=<%= product.getIdCorso() %>"><strong>-</strong></a>
                        </div>
                    </div>
                </div>
            </div>
            <% }} else {
            %>
            <p>Your cart is empty.</p>
            <%
                }
            %>

        </div>
        <div class="checkout-container">
            <a href="shop?action=addToCart&productId=1">Add Example Course 1</a><br/>
            <a href="shop?action=addToCart&productId=2">Add Example Course 2</a>
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
