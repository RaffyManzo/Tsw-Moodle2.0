<%@ page import="model.beans.Utenza" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.beans.Corso" %>
<%@ page import="model.beans.Categoria" %>
<%@ include file="../session.jsp" %>
<%--
  Created by IntelliJ IDEA.
  User: raffa
  Date: 06/07/2024
  Time: 16:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Utenza user = (Utenza) session.getAttribute("user");
%>
<html>
<head>
    <title>Crea il corso</title>
    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/footer.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/delete-margin.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/createcourse.css" rel="stylesheet">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/logo.ico">


    <!-- Importing google font -->
    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link crossorigin href="https://fonts.gstatic.com" rel="preconnect">
    <link href="https://fonts.googleapis.com/css2?family=Jura:wght@300..700&display=swap" rel="stylesheet">

    <script src="${pageContext.request.contextPath}/script/imageErrorDetect.js" defer></script>
    <script src="${pageContext.request.contextPath}/script/accountProfilePicControl.js" defer></script>
    <script src="${pageContext.request.contextPath}/script/imagePicInCreateCourse.js" defer></script>
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
                    <a href="${pageContext.request.contextPath}/account" class="header-redirect-btn"
                       id="li-header-redirect-to-profile">My account</a>
                </div>
                <div class="link-container">
                    <a href="${pageContext.request.contextPath}/logout" class="header-redirect-btn">
                        Logout
                    </a>
                </div>
            </div>
        </nav>
    </label>
    <div class="header-links" id="header-links">
        <span class="vertical-separator"></span>
        <% String initials = "";
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

        </div>
        <span class="vertical-separator"></span>
        <div class="link-container">
            <a href="${pageContext.request.contextPath}/logout" class="header-redirect-btn">
                Logout&nbsp;&nbsp;<img src="${pageContext.request.contextPath}/assets/images/log-out.png" alt="">
            </a>
        </div>
    </div>

</div>
<div class="container">
    <div class="form-box">
    <h1>Crea un Corso</h1>
    <form action="${pageContext.request.contextPath}/new-course" method="post" enctype="multipart/form-data" >
        <input type="text" id="action" name="action" value="new" style="display: none">
        <label for="titolo">Titolo del Corso</label>
        <input type="text" id="titolo" name="titolo" placeholder="Inserisci il titolo del corso" required>

        <label for="descrizione">Descrizione del Corso</label>
        <textarea id="descrizione" name="descrizione" rows="4" placeholder="Inserisci la descrizione del corso" required></textarea>

        <label for="immagine">Immagine del Corso</label>
        <div class="image-upload" id="image-upload">
            <p>+</p>
            <img id="preview" src="">
        </div>
        <input type="file" id="immagine" name="immagine" accept="image/*" style="display:none;" required>

        <label for="categoria">Categoria del Corso</label>
        <select id="categoria" name="categoria" required>
            <option value="" style="background-color: #edeeff">Categorie</option>
            <% for (Categoria c : (ArrayList<Categoria>)request.getAttribute("categories")) {%>
            <option value="<%= c.getNome()%>"><%= c.getNome()%></option>

            <%}%>
        </select>

        <label for="prezzo">Prezzo del Corso (€)</label>
        <input type="number" id="prezzo" name="prezzo" placeholder="Inserisci il prezzo del corso" min="0" step="0.01" required>

        <button type="submit" class="submit-btn">Crea Corso</button>
    </form>
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
