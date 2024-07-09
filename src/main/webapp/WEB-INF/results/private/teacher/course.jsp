<%@ page import="model.beans.Utenza" %>
<%@ page import="model.beans.Corso" %><%--
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
<html>
<head>


    <script>
        document.addEventListener("DOMContentLoaded", function () {
            const defaultImagePath = 'file?file=<%= corso.getImmagine()%>&id=<%= corso.getIdCorso()%>&c=course';
            const previewImage = document.getElementById('preview');

            fetch(defaultImagePath)
                .then(response => {
                    if (response.ok) {
                        previewImage.setAttribute('src', defaultImagePath);
                        previewImage.style.display = 'block';
                        document.querySelector('.image-upload p').style.display = 'none';
                    } else {
                        console.error('Default image not found.');
                    }
                })
                .catch(error => console.error('Error fetching default image:', error));

            document.getElementById('image-upload').addEventListener('click', function() {
                document.getElementById('immagine').click();
            });

            document.getElementById('immagine').addEventListener('change', function() {
                const file = this.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = function(event) {
                        previewImage.setAttribute('src', event.target.result);
                        previewImage.style.display = 'block';
                        document.querySelector('.image-upload p').style.display = 'none';
                    }
                    reader.readAsDataURL(file);
                }
            });

            document.querySelectorAll('.text-field-container input, .text-field-container textarea, .text-field-container select, #immagine')
                .forEach(input => {
                    input.addEventListener('input', function() {
                        document.getElementById('course-modify-submit-btn').type = 'submit';
                    });
                });
        });
    </script>

    <title>Gestione del corso <%= corso.getNome()%>
    </title>


    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/footer.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/delete-margin.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/courseTeacher.css" rel="stylesheet">


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
                    <a href="${pageContext.request.contextPath}/dashboard" class="header-redirect-btn"
                       id="li-header-redirect-to-dashboard">Vai alla tua dashboard</a>
                </div>
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
        <div class="link-container header-button dashboard-button">
            <a href="${pageContext.request.contextPath}/dashboard" class="header-redirect-btn"
               id="header-redirect-to-dashboard">Vai alla tua dashboard</a>

        </div>
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
    <form action="update-course" method="post" enctype="multipart/form-data" class="form-box course-resume">
        <input type="hidden" name="id" value="<%= corso.getIdCorso() %>">
        <h1><%= corso.getNome()%></h1>
        <div class="form-content">
            <div class="text-field-container">
                <label for="titolo">Titolo del Corso</label>
                <input type="text" id="titolo" name="titolo" placeholder="Inserisci il titolo del corso"
                       value="<%= corso.getNome()%>" required>

                <label for="descrizione">Descrizione del Corso</label>
                <textarea id="descrizione" name="descrizione" rows="4" placeholder="Inserisci la descrizione del corso"
                          required><%= corso.getDescrizione()%></textarea>

                <label for="categoria">Categoria del Corso</label>
                <select id="categoria" name="categoria" required disabled>
                    <option value="" style="background-color: #edeeff"><%= corso.getNomeCategoria()%>
                    </option>
                </select>

                <label for="prezzo">Prezzo del Corso (€)</label>
                <input type="number" id="prezzo" name="prezzo" placeholder="Inserisci il prezzo del corso" min="0"
                       step="0.01" value="<%= corso.getPrezzo()%>" required>
                <input type="hidden" id="course-modify-submit-btn" class="submit-btn" value="Conferma le modifiche"/>
            </div>
            <div class="image-upload-container">
                <label for="immagine">Immagine del corso (premi per modificare)</label>
                <div class="image-upload" id="image-upload">
                    <p>+</p>
                    <img id="preview" class="modify-preview" src="">
                </div>
                <input type="file" id="immagine" name="immagine" accept="image/*" style="display:none;">
            </div>
        </div>
    </form>
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
