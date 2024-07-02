<%--
  Created by IntelliJ IDEA.
  User: raffa
  Date: 27/06/2024
  Time: 13:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Categorie</title>

    <script src="${pageContext.request.contextPath}/script/search.js"></script>
    <link href="${pageContext.request.contextPath}/css/search.css" rel="stylesheet">
</head>
<body>
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

</body>
</html>
