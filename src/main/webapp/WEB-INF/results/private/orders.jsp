<%@ page import="model.beans.Utenza" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="session.jsp" %>
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
    <title>Lista degli Ordini</title>
    <link href="${pageContext.request.contextPath}/css/orders.css" rel="stylesheet">

    <!-- Importing google font -->
    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link crossorigin href="https://fonts.gstatic.com" rel="preconnect">
    <link href="https://fonts.googleapis.com/css2?family=Jura:wght@300..700&display=swap" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/script/imageErrorDetect.js" defer></script>
    <script src="${pageContext.request.contextPath}/script/accountProfilePicControl.js" defer></script>
</head>
<body>

<jsp:useBean id="orders" scope="request" type="java.util.ArrayList" />
<jsp:setProperty name="orders" property="*" />
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
    <c:choose>
        <c:when test="${not empty orders}">
            <c:set var="pageSize" value="10"/>
            <c:set var="totalOrders" value="${fn:length(orders)}"/>
            <c:set var="totalPages" value="${totalOrders / pageSize + (totalOrders % pageSize == 0 ? 0 : 1)}"/>
            <c:set var="currentPage" value="${param.page != null ? param.page : 1}"/>

            <c:forEach var="order" begin="${(currentPage - 1) * pageSize}" end="${currentPage * pageSize - 1}" items="${orders}" varStatus="status">
                <c:if test="${status.index < totalOrders}">
                    <div class="order-card">
                        <div class="order-info">
                            <p><strong>ID Ordine:</strong> ${order.id}</p>
                            <p><strong>Numero Carta:</strong> ${order.numeroCarta}</p>
                            <p><strong>Data Pagamento:</strong> ${order.dataPagamento}</p>
                            <p><strong>Totale:</strong> ${order.totale} €</p>
                        </div>
                        <div class="course-list">
                            <strong>Corsi Acquistati:</strong>
                            <c:forEach var="course" items="${order.corsiAcquistati}">
                                <p>${course.nome}</p>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
            </c:forEach>

            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="?page=${currentPage - 1}">Precedente</a>
                </c:if>
                <c:if test="${currentPage == 1}">
                    <a class="disabled">Precedente</a>
                </c:if>
                <c:if test="${currentPage < totalPages}">
                    <a href="?page=${currentPage + 1}">Successivo</a>
                </c:if>
                <c:if test="${currentPage == totalPages}">
                    <a class="disabled">Successivo</a>
                </c:if>
                <a href="dashboard">Torna alla dashboard</a>

            </div>

        </c:when>
        <c:otherwise>
            <p class="no-orders">Nessun ordine è stato effettuato</p>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>

