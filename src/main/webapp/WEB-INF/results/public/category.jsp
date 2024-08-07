<%@ page import="java.util.Map" %>
<%@ page import="model.beans.Corso" %>
<%@ page import="model.dao.CartDaoImpl" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="model.beans.Utenza" %>
<%@ page import="model.beans.Carrello" %>
<%@ page import="model.beans.Categoria" %>
<%@ page import="java.util.ArrayList" %>
<%@ include file="../private/session.jsp" %>

<%
    Utenza user = (Utenza) request.getSession(false).getAttribute("user");
    if(user != null) {
            Carrello carrello = new Carrello((Map<Corso, Integer>) request.getSession(false).getAttribute("cart"),
                    user.getIdUtente(), (Integer) request.getAttribute("cartID"));


            if (!carrello.getCart().isEmpty()) {


                request.getSession(false).setAttribute("cartItemCount",
                        carrello.getCart().values().stream().reduce(0, Integer::sum));
            }

    }

    Categoria categoria = (Categoria) request.getAttribute("category");
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
    <title>Categoria: <%= categoria.getNome()%></title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/logo.ico">
    <meta charset="UTF-8">
    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/delete-margin.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/footer.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/profile.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/search.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/slider.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/category.css" rel="stylesheet">

    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link crossorigin href="https://fonts.gstatic.com" rel="preconnect">
    <link href="https://fonts.googleapis.com/css2?family=Jura:wght@300..700&display=swap" rel="stylesheet">


    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/script/imageErrorDetect.js" defer></script>
    <script src="${pageContext.request.contextPath}/script/profilepic.js"></script>
    <script src="${pageContext.request.contextPath}/script/search.js"></script>
    <script src="${pageContext.request.contextPath}/script/sliderClass.js"></script>

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
    <div class="master-category-container">
        <div class="master-category-info">
            <h2><strong>Categoria:</strong> <a class="links" href="category?c=<%=categoria.getNome()%>"><%= categoria.getNome() %></a> </h2>
        </div>
    </div>
    <div class="box-info-container">
        <div class="box-info">
            <h2 class="box-info-header">Numero di corsi</h2>
            <div class="box-info-content">
                <% int courseNumber = (Integer) request.getAttribute("courseNumber"); %>
                <p><strong>Corsi nella categoria <%= categoria.getNome()%>:</strong> <%= courseNumber%></p>
            </div>
        </div>
        <div class="box-info">
            <h2 class="box-info-header">Docenti con piú corsi</h2>
            <div class="box-info-content">
                <%
                    Map<Utenza, Integer> docenti = (Map<Utenza, Integer>) request.getAttribute("categoryteacher");
                    if (docenti != null) {%>
                <%for (Utenza teacher : docenti.keySet()) {%>
                <p><strong><%= teacher.getCognome() + " " + teacher.getNome()%></strong> <%= docenti.get(teacher)%></p>
                <%}%>
                <%} else {%>
                <p>Non ci sono docenti che hanno creato un corso nella categoria <%= categoria.getNome()%></p>
                <%}%>
            </div>
        </div>
    </div>
    <h3 class="before-course-pagination">Corsi in &nbsp;<a
        class="links" href="category?c=<%=categoria.getNome()%>"><%= categoria.getNome()%>
</a></h3>
    <div class="course-pagination">
        <div id="courses">
            <%
                ArrayList<Corso> corsi = (ArrayList<Corso>) request.getAttribute("corsi");
                if (corsi != null) {
                    for (Corso corso : corsi) {
            %>
            <a class="card" href="course?courseID=<%=corso.getIdCorso()%>">
                <img src="file?file=<%= corso.getImmagine() %>&id=<%= corso.getIdCorso()%>&c=course" alt="<%= corso.getNome() %>">
                <div class="course-info">
                <p class="card-title"><%= corso.getNome() %></p>
                <p class="card-description"><%= corso.getDescrizione() %></p>
                    <p>Creato da &nbsp;<%= corso.getCreatore().getCognome() + " " + corso.getCreatore().getNome()%></p>
                    <p><%= corso.getPrezzo()%>€</p>
                </div>
            </a>
            <%
                    }
                }
            %>
        </div>
        <div class="pagination">
            <%
                int totalCourses = (int) request.getAttribute("totalCourses");
                int itemsPerPage = (int) request.getAttribute("itemsPerPage");
                int currentPage = (int) request.getAttribute("currentPage");
                int totalPages = (int) Math.ceil((double) totalCourses / itemsPerPage);

                for (int i = 1; i <= totalPages; i++) {
                    String linkClass = (i == currentPage) ? "active" : "";
            %>
            <a href="?c=<%= request.getParameter("c") %>&page=<%= i %>" class="<%= linkClass %>"><%= i %></a>
            <%
                }
            %>
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
