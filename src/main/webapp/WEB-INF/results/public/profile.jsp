<%@ page import="model.beans.Utenza" %>
<%@ page import="model.beans.Carrello" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.beans.Corso" %>
<%@ page import="model.dao.CartDaoImpl" %>
<%@ page import="java.sql.SQLException" %>
<%
    Utenza user = (Utenza) request.getSession(false).getAttribute("user");
    if(user != null) {
        try {
            Carrello carrello = new Carrello((Map<Corso, Integer>) request.getSession(false).getAttribute("cart"),
                    user.getIdUtente(), new CartDaoImpl().getCartIDByUser(user.getIdUtente()));


            if (!carrello.getCart().isEmpty()) {


                request.getSession(false).setAttribute("cartItemCount",
                        carrello.getCart().values().stream().reduce(0, Integer::sum));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Utenza profile = (Utenza) request.getAttribute("profile");
%>

<%-- Debugging --%>
<%
    if (user != null) {
        System.out.println("User found: " + user.getNome() + " " + user.getCognome());
    } else {
        System.out.println("User not found in session");
    }
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><%= profile.getNome() + " " + profile.getCognome()%></title>

    <meta charset="UTF-8">
    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/delete-margin.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/footer.css" rel="stylesheet">

    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link crossorigin href="https://fonts.gstatic.com" rel="preconnect">
    <link href="https://fonts.googleapis.com/css2?family=Jura:wght@300..700&display=swap" rel="stylesheet">


    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/script/profilepic.js"></script>
    <script src="${pageContext.request.contextPath}/script/search.js"></script>
    <link href="${pageContext.request.contextPath}/css/search.css" rel="stylesheet">
</head>
<body>
<div class="header" id="header">
    <!-- <div class="link-container">
        <a href="${pageContext.request.contextPath}/AdminServlet"
           class="header-redirect-btn" >admin </a>
    </div>!-->

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
                       class="header-redirect-btn" id="li-header-redirect-to-cart">Carrello (${sessionScope.cartItemCount != null && sessionScope.cartItemCount > 0 ? sessionScope.cartItemCount : '0'})</a>
                </div>
                <%if(user != null) {%>
                <div class="link-container">
                    <a href="${pageContext.request.contextPath}/logout" class="header-redirect-btn">
                        Logout
                    </a>
                </div>
                <%}%>
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
                <img src="${pageContext.request.contextPath}/file?file=${user.getImg()}&id=${user.getIdUtente()}&c=user" alt="<%= initials %>" id="profile-pic">
                <div class="initials" style="display: none;"><%= initials %></div></a>

            <% } else { %>
            <div class="link-container header-button registration-button">
                <a href="${pageContext.request.contextPath}/registrazione.html" class="header-redirect-btn"
                   id="header-redirect-to-registration">Registrati</a><%}%>

            </div>
            <span class="vertical-separator"></span>

            <div class="link-container">
                <a href="${pageContext.request.contextPath}/shop?action=viewCart" class="header-redirect-btn" id="header-redirect-to-cart">
                    <img
                            src="${pageContext.request.contextPath}/assets/images/shopping-basket.png" alt="">
                    <p>${sessionScope.cartItemCount != null && sessionScope.cartItemCount > 0 ? sessionScope.cartItemCount : '0'}</p>
                </a>
            </div>
            <%if(user != null) {%>
            <span class="vertical-separator"></span>
            <div class="link-container">
                <a href="${pageContext.request.contextPath}/logout" class="header-redirect-btn">
                    Logout&nbsp;&nbsp;<img src="${pageContext.request.contextPath}/assets/images/log-out.png" alt="">
                </a>
            </div>
            <%}%>
        </div>

    </div>
</div>

    <div class="search-bar-container">
        <div class="search-box">
            <button id="search-button">
                <img id="search-image" src="${pageContext.request.contextPath}/assets/images/lens.png" alt="Cerca">
            </button>
            <input type="text" id="search-bar" placeholder="Cerca corsi, categorie o docenti...">

        </div>
        <div id="dropdown" class="dropdown-content">
            <ul id="search-results"></ul>
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
