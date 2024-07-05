<%--
  Created by IntelliJ IDEA.
  User: raffa
  Date: 05/07/2024
  Time: 16:29
  To change this template use File | Settings | File Templates.
--%>
<%
    HttpSession checkSession = request.getSession(false);
    if (checkSession != null) {
        if(checkSession.getAttribute("user") != null) {
%>
<html>
<head>
    <title></title>
    <script src="${pageContext.request.contextPath}/script/sessionPopUp.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sessionPopUp.css">
</head>
<body>
<div id="sessionPopup" class="popup">
    <div class="popup-header">
        La tua sessione sta per scadere
        <span class="close-button" onclick="hidePopup()">X</span>
    </div>
    <div class="popup-content">
        <p>La tua sessione online scadra' tra <span id="countdown">1 min 2 sec</span>.</p>
        <p>Per favore, clicca "Rinnova" per continuare a lavorare<br> oppure clicca "Esci" per terminare la sessione ora.</p>
    </div>
    <div class="popup-footer">
        <button class="continue-button" onclick="renewSession()">Rinnova</button>
        <button class="logoff-button" onclick="logOff()">Esci</button>
    </div>
</div>
</body>
</html>
<%} }%>
