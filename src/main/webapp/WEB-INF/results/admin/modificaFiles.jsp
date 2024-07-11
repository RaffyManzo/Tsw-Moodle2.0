<%@ page import="java.util.List" %>
<%@ page import="model.beans.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Learn Hub - Area admin</title>
    <link href="${pageContext.request.contextPath}/css/header.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/admin.css" rel="stylesheet">
</head>
<body>
<div class="header" id="header">
    <div class="header-main-info" id="header-main-info">
        <a href="${pageContext.request.contextPath}/admin" class="logo-image" id="header-logo-image">
            <img src="${pageContext.request.contextPath}/assets/images/logo.png">
        </a>
        <h1 class="site-name" id="header-site-name">
            Learn Hub
        </h1>
        <h1 class="admin-header">Area admin</h1>
    </div>
</div>
<div id="table-container">
    <%
        List<String> files = (List<String>) request.getAttribute("elemento");

        if (files != null && !files.isEmpty()) {
    %>

    <table>
        <thead>
        <tr>
            <th>Nome</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (String file : files) {
                System.out.println(file + "awa\n");
        %>
        <tr>
            <td><%= file %></td>
            <td>
                <div class="button-container">
                    <form action="VisualizzaFile">
                        <input type="hidden" name="idCorsoV" value="<%= Integer.parseInt(request.getAttribute("corso").toString()) %>">
                        <input type="hidden" name="idArgomento" value="<%= Integer.parseInt(request.getAttribute("idArgomento").toString())  %>"/>
                        <input type="hidden" name="idLezione" value="<%= Integer.parseInt(request.getAttribute("idLezione").toString()) %>"/>
                        <input type="hidden" name="type" value="<%= file %>"/>
                        <input type="submit" value="Elimina">
                    </form>
                </div>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <p>Non ci sono file per questo argomento</p>
        <%
            }
        %>
        </tbody>
    </table>
    <form action="VisualizzaArgomenti">
        <input type="hidden" name="type" value="view"/>
        <input type="hidden" name="idCorsoV" value="<%= Integer.parseInt(request.getAttribute("corso").toString()) %>">
        <input type="hidden" name="idLezione" value="<%= Integer.parseInt(request.getAttribute("idLezione").toString()) %>"/>
        <input type="submit" value="Indietro">
    </form>
</div>
</body>
</html>