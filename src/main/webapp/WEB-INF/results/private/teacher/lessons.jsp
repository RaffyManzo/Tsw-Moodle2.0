<%@ page import="model.beans.Argomento" %>
<%@ page import="model.beans.Lezione" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.beans.Utenza" %>
<%@ page import="model.beans.Corso" %>
<%@ page import="model.beans.Argomento" %>
<%@ page import="model.beans.Lezione" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: raffa
  Date: 09/07/2024
  Time: 12:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Corso corso = (Corso) request.getAttribute("course");
    Utenza user = (Utenza) request.getSession(false).getAttribute("user");
%>
<%@ include file="../session.jsp" %>

<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/logo.ico">

<title>Aggiungi una lezione</title>

<script src="${pageContext.request.contextPath}/script/newLessons.js" defer></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/lesson.css">

<link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/course.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/footer.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/delete-margin.css" rel="stylesheet">



<!-- Importing google font -->
<link href="https://fonts.googleapis.com" rel="preconnect">
<link crossorigin href="https://fonts.gstatic.com" rel="preconnect">
<link href="https://fonts.googleapis.com/css2?family=Jura:wght@300..700&display=swap" rel="stylesheet">

<script src="${pageContext.request.contextPath}/script/accountProfilePicControl.js" defer></script>
<script src="${pageContext.request.contextPath}/script/imageErrorDetect.js" defer></script>
<script src="${pageContext.request.contextPath}/script/accountProfilePicControl.js" defer></script>


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
<div class="content-wrapper">
    <form class="new-lesson-wrapper" id="form-on-lesson" action="lesson" method="post">
        <%
            Lezione lezione = (Lezione) request.getAttribute("lezione");
        %>
        <input type="hidden" name="courseId" id="courseId" value="<%= corso.getIdCorso() %>">
        <input type="hidden" name="action" id="action" value="createLesson">

        <h2><%= lezione != null ? lezione.getTitolo() : "Nuova lezione"%></h2>
        <label for="lessonTitle">Titolo della Lezione:</label>
        <input type="text" id="lessonTitle" name="lessonTitle" value="<%= lezione != null ? lezione.getTitolo() : ""%>" <% if(lezione == null) {%>required <%}%>><br><br>
        <label for="lessonDescription">Descrizione della Lezione:</label>
        <textarea id="lessonDescription" name="lessonDescription" <% if(lezione == null) {%> required <%}%>><%if(lezione != null){%><%= lezione.getDescrizione()%><%}%></textarea><br><br>
        <div class="button-container">


        <%if (lezione != null) {%>
            <button type="button" id="createLessonButton">Nuovo argomento</button>
        <button type="submit" id="save">Salva modifiche</button>
            <button type="button" id="delete-lesson" class="delete-lesson">Elimina lezione</button>
        <input type="hidden" name="lessonID" id="lessonid" value="<%= lezione.getId() %>">
            <button type="button" onclick="window.location = '${pageContext.request.contextPath}/course?courseID=<%= corso.getIdCorso()%>'">Indietro</button>
        <%} else {%>
            <button type="submit">Nuova lezione</button>
            <button type="button" onclick="window.history.back()">Indietro</button>
            <%}%>

        </div>
    </form>

    <div class="lessons-wrapper">
        <h2>Argomenti</h2>
        <%if(lezione != null) {
            System.out.println(lezione.getId());
        for(Argomento argomento : lezione.getArgomenti()) {%>
        <form class="lesson-form" method="post" action="lesson" enctype="multipart/form-data">
        <div class="accordion">
            <input type="hidden" name="courseId" value="<%= lezione.getIdCorso() %>">
            <input type="hidden" name="lessonID" value="<%= lezione.getId() %>">
            <input type="hidden" name="action" value="createTopic">
            <input type="text" name="topicTitle" value="<%= argomento.getNome()%>">
            <input type="hidden" name="idargomento" value="<%= argomento.getId()%>">
        </div>
        <div class="panel">
            <textarea name="topicDescription"><%= argomento.getDescrizione()%></textarea>
            <div class="file-upload" onclick="this.querySelector('.fileInput').click();">
                <input type="file" name="file" class="fileInput" style="display: none;" accept=".pdf,.txt" onchange="displayFileDetails(this)">
                <p>+</p>
            </div>
            <div class="fileDetailsContainer">
                <div class="file-details">
                    <span class="file-icon"><img src="${pageContext.request.contextPath}/assets/images/file-text.png" alt="📄"></span>
                    <% if(!argomento.getFilenames().isEmpty()) {%>
                    <a target="_blank" href="file?file=<%= argomento.getFilenames().get(0)%>&id=<%= corso.getIdCorso()%>&c=course" ><%= argomento.getFilenames().get(0)%></a>
                    <%}%>
                </div>
            </div>
            <div class="button-container">
                <button type="submit" class="save-topic">Salva modifiche</button>
                <button type="button" class="delete-topic">Elimina argomento</button>
            </div>
        </div>
        </form>
        <%}
        }%>
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

