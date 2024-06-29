<%@ page import="model.beans.Utenza" %><%--
  Created by IntelliJ IDEA.
  User: raffa
  Date: 27/06/2024
  Time: 17:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Utenza user = (Utenza) session.getAttribute("user");
%>
<html>
<head>
    <title>Account - <%= user.getNome()%>
    </title>

    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/reg.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/account.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link crossorigin href="https://fonts.gstatic.com" rel="preconnect">
    <link href="https://fonts.googleapis.com/css2?family=Jura:wght@300..700&display=swap" rel="stylesheet">


    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script defer>
        $(document).ready(() => {
            const image = document.getElementById('profile-pic');

            image.onerror = function () {
                console.log("Image failed to load, loading default image.");
                image.src = 'file?file=default.png&c=course';
            };

            image.onload = function () {
                if (image.naturalHeight + image.naturalWidth === 0) {
                    console.log("Image loaded but has zero size, triggering onerror.");
                    image.onerror();
                } else {
                    console.log("Image loaded successfully.");
                }
            };

            // For cached images that might not trigger onload
            if (image.complete) {
                if (image.naturalHeight + image.naturalWidth === 0) {
                    console.log("Image already complete but has zero size, triggering onerror.");
                    image.onerror();
                } else {
                    console.log("Image already complete and loaded successfully.");
                }
            } else {
                console.log("Image is not complete, waiting for load event.");
            }
        });

        function imageNotFound() {
            console.log("Image doesn't load, loaded a placeholder");
        }




    </script>
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
                       id="li-header-redirect-to-dashboard">Vai alla tua dashboard</a>

                    <% } else { %>
                    <a href="${pageContext.request.contextPath}/login.html" class="header-redirect-btn"
                       id="header-redirect-to-login">Login</a> <%}%>
                    <% %>
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
        <div class="link-container">
            <a href="${pageContext.request.contextPath}/logout" class="header-redirect-btn">
                Logout&nbsp;&nbsp;<img src="${pageContext.request.contextPath}/assets/images/log-out.png" alt="">
            </a>
        </div>
    </div>
</div>
<div class="container">
    <div class="header-section">
        <h1>Gestisci account</h1>
        <h6 class="line-divider"></h6>
    </div>
    <div class="section">
        <div class="profile-pic-container">
            <div class="profile-pic-wrapper">
                <img class="profile-pic"
                     src="${pageContext.request.contextPath}/file?file=${user.getImg()}&id=${user.getIdUtente()}&c=user"
                     id="profile-pic" alt="">
            </div>
            <form class="modify-pic" action="${pageContext.request.contextPath}/uploadImage" method="post" enctype="multipart/form-data" id="uploadForm">
                <input type="file" id="profilePicture" name="profilePicture" accept="image/*" onchange="document.getElementById('uploadForm').submit()">
                <label for="profilePicture">
                    <img src="${pageContext.request.contextPath}/assets/images/pen-line.png" alt="Edit" class="edit-icon">
                    <span>Modifica immagine</span>
                </label>
            </form>
        </div>
        <div class="fields-set-column">
            <div class="field-row-container">
                <div class="input-box">
                    <div class="field-input-box-container ">
                        <label>Nome</label>
                        <input class="input-field" readonly type="text" value="<%= user.getNome()%>">
                    </div>
                </div>
                <div class="input-box">
                    <div class="field-input-box-container ">
                        <label>Cognome</label>
                        <input class="input-field" readonly type="text" value="<%= user.getCognome()%>">
                    </div>
                </div>
            </div>
            <div class="field-row-container">
                <div class="input-box">
                    <div class="field-input-box-container ">
                        <label>Data di nascita</label>
                        <input class="input-field" readonly type="date" value="<%= user.getDataNascita()%>">
                    </div>
                </div>
                <div class="input-box">
                    <div class="field-input-box-container ">
                        <label>Nazione</label>
                        <input class="input-field" readonly type="text" value="<%= user.getCitta()%>">
                    </div>
                </div>

            </div>
            <div class="field-row-container ">
                <div class="input-box all-row">
                    <div class="field-input-box-container ">
                        <label>Indirizzo</label>
                        <input class="input-field" readonly type="text" value="<%= user.getIndirizzo()%>">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="header-section">
        <h1>Dati di accesso</h1>
        <h6 class="line-divider"></h6>
    </div>
    <div class="section">
        <div class="fields-set-column fields-set-column-all-row">
            <div class="field-row-container">
                <div class="input-box">
                    <div class="field-input-box-container ">
                        <label>Email</label>
                        <input class="input-field" readonly type="email" value="<%= user.getEmail()%>">
                    </div>
                </div>
                <form action="${pageContext.request.contextPath}/account" method="POST" class="input-box input-box-button">
                    <div class="field-input-box-container input-field-button">
                        <label>Telefono</label>
                        <input class="input-field" name="t" type="text" value="<%= user.getTelefono()%>">
                    </div>
                    <button class="edit-icon-button" type="submit"><img src="${pageContext.request.contextPath}/assets/images/pen-line.png" alt="Edit" class="edit-icon"></button>
                </form>
            </div>
            <h3>Modifica password </h3>
            <form action="${pageContext.request.contextPath}/account" METHOD="post" class="field-row-container">
                <div class="input-box">
                    <div class="field-input-box-container ">
                        <label>Password</label>
                        <input class="input-field" type="password">
                    </div>
                </div>
                <div class="input-box">
                    <div class="field-input-box-container ">
                        <label>Conferma password</label>
                        <input class="input-field" type="password">
                    </div>
                </div>
                <button type="submit" class="confirm-password-btn"><img
                        src="${pageContext.request.contextPath}/assets/images/check.png" alt="">
                </button>
            </form>
        </div>
    </div>
    <div class="header-section">
        <h1>Operazioni sull’account</h1>
        <h6 class="line-divider"></h6>
    </div>
    <div class="section not-resize">
        <div class="fields-set-column fields-set-column-all-row">
            <form method="post" action="${pageContext.request.contextPath}/account" class="field-row-container">
                <button type="submit" class='delete-account-btn red-btn' onclick="return confirmDeleteAccount()">Elimina account</button>
                <p class="delete-message">
                    É un’operazione irreversibile, tutti i <br>
                    dati e corsi da te creati verranno<br>
                    eliminati
                </p>
                <input style="display: none; width: 0; height: 0" name="d" value="TRUE">
                <script>
                function confirmDeleteAccount() {
                    return confirm("Sei sicuro di voler eliminare l'account?");
                }
            </script>

            </form>
        </div>
    </div>
</div>
</body>
</html>
