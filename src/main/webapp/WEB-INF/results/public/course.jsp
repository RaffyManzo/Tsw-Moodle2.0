<%@ page import="java.util.ArrayList" %>
<%@ include file="../private/session.jsp" %>

<%@ page import="model.beans.*" %><%--
  Created by IntelliJ IDEA.
  User: raffa
  Date: 26/06/2024
  Time: 23:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Corso corso = (Corso) request.getAttribute("course");
    Utenza user = (Utenza) request.getSession(false).getAttribute("user");

    request.getSession().setAttribute("c", corso.getCreatore().getIdUtente());
%>
<html>
<head>
    <title><%= corso.getNome() %>
    </title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/script/sliderClass.js"></script>
    <script src="${pageContext.request.contextPath}/script/profilepic.js"></script>
    <script src="${pageContext.request.contextPath}/script/lessonsCoursePage.js"></script>
    <link href="${pageContext.request.contextPath}/css/slider.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/script/search.js"></script>
    <link href="${pageContext.request.contextPath}/css/search.css" rel="stylesheet">    <script src="${pageContext.request.contextPath}/script/sessionPopUp.js"></script>


    <script src="${pageContext.request.contextPath}/script/imageErrorDetect.js" defer></script>


    <script>
        $(document).ready(function () {
            // Creazione di un'istanza della classe CourseSlider
            // Passa il parametro dinamico alla tua classe JavaScript
            let query = "getCoursesJson?action=byCreator"

            // Crea un'istanza di CourseSlider con i parametri dinamici
            new CourseSlider("#other-course-of-creator", query);

        })
    </script>

    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/delete-margin.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/slider.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/footer.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/course.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link crossorigin href="https://fonts.gstatic.com" rel="preconnect">
    <link href="https://fonts.googleapis.com/css2?family=Jura:wght@300..700&display=swap" rel="stylesheet">
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
                       class="header-redirect-btn" id="li-header-redirect-to-cart">Carrello
                        (${sessionScope.cartItemCount != null && sessionScope.cartItemCount > 0 ? sessionScope.cartItemCount : '0'})</a>
                </div>
                <%if (user != null) {%>
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
            <span class="vertical-separator"></span>

            <div class="link-container">
                <a href="${pageContext.request.contextPath}/shop?action=viewCart" class="header-redirect-btn"
                   id="header-redirect-to-cart">
                    <img
                            src="${pageContext.request.contextPath}/assets/images/shopping-basket.png" alt="">
                    <p>${sessionScope.cartItemCount != null && sessionScope.cartItemCount > 0 ? sessionScope.cartItemCount : '0'}</p>
                </a>
            </div>
            <%if (user != null) {%>
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

<div class="content-container">
    <div class="section-top">
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
        <div class="overlayed add-to-cart-container">
            <img src="file?file=<%= corso.getImmagine() %>&id=<%= corso.getIdCorso()%>&c=course" alt="">
            <h2>Prezzo: <strong><%= corso.getPrezzo()%> € <%if(request.getAttribute("isPurchased") != null) {%><%= (Boolean)request.getAttribute("isPurchased") ? "(Acquistato)": ""%><%}%>
            </strong></h2>
            <a href="shop?action=addToCart&productId=<%=corso.getIdCorso()%>" class="add-to-cart-button">Aggiungi al
                carrello</a>
        </div>
    </div>

    <div class="lessons-container">
        <%if(request.getAttribute("lezioni") != null) {%>
        <h2>Contenuto del corso</h2>
        <% for(Lezione lezione : (ArrayList<Lezione>)request.getAttribute("lezioni")) {%>
        <button class="accordion"><%= lezione.getTitolo()%></button>

        <div class="panel">
        <% for(Argomento argomento : lezione.getArgomenti()) {%>

            <p><%= argomento.getNome()%></p>

        <%}%>
        </div>
        <%}%>

        <%} else {%>
        <p>Il corso ha né lezioni ne argomenti... <br>
            Attendi vengano caricati da <%= corso.getCreatore().getNome() + " " + corso.getCreatore().getCognome()%></p>
        <%}%>

    </div>
    <div class="course-creator-section">
        <%
            Utenza creator = corso.getCreatore();
        %>

        <div class="creator-info-container">
            <a href="${pageContext.request.contextPath}/profile?id=<%= creator.getIdUtente() %>" class="creator-profile-pic">
                <%
                String initials = "";
                if (creator.getNome() != null && creator.getCognome() != null) {
                initials = creator.getNome().charAt(0) + "" + creator.getCognome().charAt(0);
                }
                %>
                <img src="${pageContext.request.contextPath}/file?file=<%= creator.getImg()%>&id=<%= creator.getIdUtente()%>&c=user"
                     alt="<%= initials %>" id="creator-profile-pic">
                <div class="initials" style="display: none;"><%= initials %></div>
            </a>
            <div class="creator-info">
                <h5>Docente: <%= creator.getNome() + " " + creator.getCognome()%></h5>
                <p>Email: &nbsp; <%= creator.getEmail()%></p>
            </div>


        </div>
        <div class="courses-section" id="course-section">

            <h3>Altri corsi di <a
                    class="course-links dark-link"><%= corso.getCreatore().getCognome() + " " + corso.getCreatore().getNome()%>
            </a></h3>
            <div class="slider" >
                <button class="move-slider-button move-slider-left">
                    <img src="${pageContext.request.contextPath}/file?file=leftarr.png&c=app" alt="">
                </button>
                <div class="slider-element-container" id="other-course-of-creator">

                </div>
                <button class="move-slider-button move-slider-right">
                    <img src="${pageContext.request.contextPath}/file?file=rightarr.png&c=app" alt="">
                </button>
            </div>
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
