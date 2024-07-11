<%@ page import="java.util.Map" %>
<%@ page import="model.dao.CartDaoImpl" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="model.dao.UtenzaDaoImpl" %>
<%@ page import="model.dao.CategoriaDaoImpl" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.beans.*" %>
<%@ include file="../private/session.jsp" %>

<%
    Corso corso = (Corso) request.getAttribute("course");
    Utenza user = (Utenza) request.getSession(false).getAttribute("user");
    if(user != null) {
        Carrello carrello = new Carrello((Map<Corso, Integer>) request.getSession(false).getAttribute("cart"),
                user.getIdUtente(), (Integer) request.getAttribute("cartID"));


        if (!carrello.getCart().isEmpty()) {


            request.getSession(false).setAttribute("cartItemCount",
                    carrello.getCart().values().stream().reduce(0, Integer::sum));
        }
    }
%>

<%-- Debugging --%>
<%
    if (user != null) {
        System.out.println("User found: " + user.getNome() + " " + user.getCognome());
    } else {
        System.out.println("User not found in session");
    }
%>
<html>
<head>
    <title>Corso - <%= corso.getNome()%></title>
    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/footer.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/delete-margin.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/search.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/course.css" rel="stylesheet">

    <!-- Importing google font -->
    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link crossorigin href="https://fonts.gstatic.com" rel="preconnect">
    <link href="https://fonts.googleapis.com/css2?family=Jura:wght@300..700&display=swap" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/script/profilepic.js"></script>
    <script src="${pageContext.request.contextPath}/script/search.js"></script>
    <script src="${pageContext.request.contextPath}/script/saveInLocalStorageTheLastVisitedFile.js"></script>
    <script src="${pageContext.request.contextPath}/script/lessonsCoursePage.js"></script>

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
                <% %>

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

        <div class="select">
            <!-- country names and country code -->
            <select class="categories" name="category">
                <option value="" style="background-color: #edeeff">Categorie</option>
                <% for (Categoria c : (ArrayList<Categoria>)request.getAttribute("categories")) {%>
                <option onclick='window.location = "category?c=" + "<%= c.getNome() %>"' value=''><%= c.getNome()%></option>

                <%}%>
            </select>
        </div>
    </div>
    <div class="container">
        <div class="section-top" style="margin-top: 100px;padding: 30px;">
            <div class="course-info">
                <p style="font-size: 1.4rem;"><a class="course-links">Tutte le categorie</a> >
                    <a href="category?c=<%= corso.getNomeCategoria()%>"
                       class="course-links"><%= corso.getNomeCategoria()%>
                    </a>
                </p>


                <div>
                    <h2 class="course-name"><%= corso.getNome()%>
                    </h2>
                    <p class="course-description"><%= corso.getDescrizione()%>
                    </p>
                    <p>Creato da &nbsp; <a href="profile?id=<%= corso.getCreatore().getIdUtente() %>"
                                           class="course-links"><%= corso.getCreatore().getCognome() + " " + corso.getCreatore().getNome()%>
                    </a></p>
                </div>
            </div>
            <div class="add-to-cart-container" style="height: 70%; width: auto">
                <img style="height: 100%; object-fit: fill" src="file?file=<%= corso.getImmagine() %>&id=<%= corso.getIdCorso()%>&c=course" alt="">
            </div>
        </div>
        <div class="lessons-container">
            <%if(request.getAttribute("lezioni") != null) {%>
            <h2>Contenuto del corso</h2>
            <% for(Lezione lezione : (ArrayList<Lezione>)request.getAttribute("lezioni")) {%>
            <button class="accordion">
                <p><%= lezione.getTitolo()%></p>
            </button>

            <div class="panel">
                <% for(Argomento argomento : lezione.getArgomenti()) {%>

                <div class="argomento">
                    <div class="argomento-header">
                        <h3><%= argomento.getNome() %></h3>

                    </div>
                    <div class="argomento-content">
                        <p><%= argomento.getDescrizione() %></p>
                        <% if(!argomento.getFilenames().isEmpty()) { %>
                        <p style="display: none" class="lessonid"><%= argomento.getLezione()%></p>
                        <p style="display: none" class="topicid"><%= argomento.getId()%></p>
                        <a target="_blank" class="resource" href="file?file=<%= argomento.getFilenames().get(0)%>&id=<%= corso.getIdCorso()%>&c=course">
                            <p class="resource-pic"><img src="${pageContext.request.contextPath}/assets/images/file-text.png" alt="📄"></p>
                            <p><%= argomento.getFilenames().get(0)%></p>
                        </a>
                        <% } %>
                    </div>
                </div>

                <%}%>
            </div>
            <%}%>

            <%} else {%>
            <p>Carica delle lezioni, non ce ne sono</p>
            <%}%>
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
