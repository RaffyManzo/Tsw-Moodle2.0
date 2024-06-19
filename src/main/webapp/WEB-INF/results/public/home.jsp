<%@ page import="model.beans.Utenza" %>
<%
    Utenza user = (Utenza) session.getAttribute("User");
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
    <title>Learn Hub</title>
    <meta charset="UTF-8">
    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/delete-margin.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/slider.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/home.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/footer.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link crossorigin href="https://fonts.gstatic.com" rel="preconnect">
    <link href="https://fonts.googleapis.com/css2?family=Jura:wght@300..700&display=swap" rel="stylesheet">


    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/script/sliderClass.js"></script>
    <script src="${pageContext.request.contextPath}/script/bannerSize.js"></script>
    <script src="${pageContext.request.contextPath}/script/someDisplayFix.js" defer></script>
    <script src="${pageContext.request.contextPath}/script/headerAccessBehaviour.js"></script>

    <script>
        $(document).ready(function () {
            // Creazione di un'istanza della classe CourseSlider
            const courseSliderTrend = new CourseSlider("#trend-course-slider-container", 'courses');
            const courseSliderYour = new CourseSlider("#your-course-slider-container", 'courses');

        })
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
            <img src="${pageContext.request.contextPath}/file?file=${user.getImg()}&id=${user.getIdUtente()}&c=user" alt="<%= initials %>" id="profile-pic">
            <div class="initials" style="display: none;"><%= initials %></div></a>

        <% } else { %>
            <div class="link-container header-button registration-button">
        <a href="${pageContext.request.contextPath}/registrazione.html" class="header-redirect-btn"
           id="header-redirect-to-registration">Registrati</a><%}%>
            <% %>

        </div>
        <span class="vertical-separator" id="vertical-bar-tofix"></span>

        <div class="link-container">
            <a href="${pageContext.request.contextPath}/shop?action=viewCart" class="header-redirect-btn" id="header-redirect-to-cart"><img
                    src="${pageContext.request.contextPath}/assets/images/shopping-basket.png" alt=""></a>
        </div>
    </div>
</div>
<div class="banner">
    <div class="banner-rect-slider" id="banner-rect-slider">
        <div class="banner-rect-slider-overlay"></div>
        <div id="banner-rect-slider-content"></div>
    </div>
</div>
<div class="container">
    <div class="section-header">
        <img src="${pageContext.request.contextPath}/file?file=flame%201.png&c=app" alt="">
        <label>Corsi di tendenza: il meglio selezionato per te</label>
    </div>
    <div class="courses-section">
        <div class="slider" id="trend-courses">
            <button class="move-slider-button move-slider-left">
                <img src="${pageContext.request.contextPath}/file?file=leftarr.png&c=app" alt="">
            </button>

            <div class="slider-element-container" id="trend-course-slider-container">

            </div>
            <button class="move-slider-button move-slider-right">
                <img src="${pageContext.request.contextPath}/file?file=rightarr.png&c=app" alt="">
            </button>
        </div>
    </div>
    <h6 class="line-divider"></h6>
    <div class="section-header">
        <img src="${pageContext.request.contextPath}/file?file=star.png&c=app" alt="">
        <label>Corsi che potrebbero piacerti: in categoria preferita</label>
    </div>
    <div class="courses-section" id="course-section">

        <div class="slider" id="your-courses">
            <button class="move-slider-button move-slider-left">
                <img src="${pageContext.request.contextPath}/file?file=leftarr.png&c=app" alt="">
            </button>
            <div class="slider-element-container" id="your-course-slider-container">

            </div>
            <button class="move-slider-button move-slider-right">
                <img src="${pageContext.request.contextPath}/file?file=rightarr.png&c=app" alt="">
            </button>
        </div>
    </div>
    <h6></h6>

    <div class="section-header">
        <img src="${pageContext.request.contextPath}/assets/images/list.png" alt="">
        <label>Categorie principali</label>
    </div>
    <div class="categories-container" id="categories-container">
        <div class="categories-inner-row-container">
            <div class="category-circle-box">
                <a href="">
                    <img src="${pageContext.request.contextPath}/assets/images/sc.png" alt="">
                </a>
            </div>
            <div class="category-circle-box">
                <a href="">
                    <img src="${pageContext.request.contextPath}/assets/images/sviluppoweb.png" alt="">
                </a>
            </div>
            <div class="category-circle-box">
                <a href="">
                    <img src="${pageContext.request.contextPath}/assets/images/sviluppoGiochi.png" alt="">
                </a>
            </div>
            <div class="category-circle-box">
                <a href="">
                    <img src="${pageContext.request.contextPath}/assets/images/backend.png" alt="">
                </a>
            </div>
        </div>
        <div class="categories-inner-row-container">
            <div class="category-circle-box">
                <a href="">
                    <img src="${pageContext.request.contextPath}/assets/images/foto.png" alt="">
                </a>
            </div>
            <div class="category-circle-box">
                <a href="">
                    <img src="${pageContext.request.contextPath}/assets/images/trading.png" alt="">
                </a>
            </div>
            <div class="category-circle-box">
                <a href="">
                    <img src="${pageContext.request.contextPath}/assets/images/marketing.png" alt="">
                </a>
            </div>
            <div class="category-circle-box">
                <a href="">
                    <img src="${pageContext.request.contextPath}/assets/images/musica.png" alt="">
                </a>
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
