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
    <title>Account - <%= user.getNome()%></title>
</head>
<body>

</body>
</html>
