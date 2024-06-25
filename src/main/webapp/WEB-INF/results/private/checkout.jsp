<%@ page import="model.beans.Corso" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.beans.Utenza" %><%--
  Created by IntelliJ IDEA.
  User: raffa
  Date: 25/06/2024
  Time: 15:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Utenza user = (Utenza) session.getAttribute("user");
    Map<Corso, Integer> carrello = (Map<Corso, Integer>) request.getSession(false).getAttribute("cart");
%>
<html>
<head>
    <title>Pagamento</title>

    <link href="${pageContext.request.contextPath}/css/footer.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link href="${pageContext.request.contextPath}/css/delete-margin.css" rel="stylesheet">
    <link crossorigin href="https://fonts.gstatic.com" rel="preconnect">
    <link href="${pageContext.request.contextPath}/css/checkout.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Jura:wght@300..700&display=swap" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/script/imageErrorDetect.js"></script>
    <script src="${pageContext.request.contextPath}/script/checkout.js"></script>
</head>
<body>
<div class='container'>
    <div class='window'>
        <div class='order-info'>
            <div class='order-info-content'>
                <h2>Riepilogo ordine</h2>

                <%

                    if (carrello != null && !carrello.isEmpty()) {
                        for (Map.Entry<Corso, Integer> entry : carrello.entrySet()) {
                            Corso product = entry.getKey();
                            Integer quantity = entry.getValue();
                %>
                <div class='line'></div>
                <table class='order-table'>
                    <tbody>
                    <tr>
                        <td><img src="file?file=<%= product.getIdCorso() + "/" + product.getImmagine() %>&c=course"
                                 alt="/" class='full-width'></img>
                        </td>
                        <td>
                            <br> <span class='thin'><%= product.getNome()%></span>
                            <br> <%= product.getNomeCategoria()%><br> <span
                                class='thin small'> Quantitá: <%= quantity %><br><br></span>
                        </td>

                    </tr>
                    <tr>
                        <td>
                            <div class='price'><%= product.getPrezzo() * quantity%> $</div>
                        </td>
                    </tr>
                    </tbody>

                </table>
                <%
                        }
                    }
                %>


            </div>
            <div class='total'>
                <span style='float:left;'>TOTALE da pagare</span>
                <span style='float:right; text-align:right;'>
                <%
                    double price = 0;
                    if (carrello != null) {
                        for (Map.Entry<Corso, Integer> entry : carrello.entrySet()) {
                            Corso corso = entry.getKey();
                            int quantity = entry.getValue();
                            price += corso.getPrezzo() * quantity;
                        }
                    }
                %>
                <%= Math.round(price * 100.0) / 100.0 %> $
                    </span>
            </div>
        </div>
        <form class='credit-info' onsubmit="validateForm()">
            <div class='credit-info-content'>
                <table class='half-input-table'>
                    <tr>
                        <td>Seleziona il tipo di carta:</td>
                        <td>
                            <div class='dropdown' id='card-dropdown'>
                                <div class='dropdown-btn' id='current-card'>Visa</div>
                                <div class='dropdown-select'>
                                    <ul>
                                        <li>Master Card</li>
                                        <li>American Express</li>
                                    </ul>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
                <img src='https://dl.dropboxusercontent.com/s/ubamyu6mzov5c80/visa_logo%20%281%29.png' height='80'
                     class='credit-card-image' id='credit-card-image'>
                Numero di carta
                <input class='input-field'></input>
                Intestatario della carta
                <input class='input-field'></input>
                <table class='half-input-table'>
                    <tr>
                        <td> Data di scadenza
                            <input class='input-field'></input>
                        </td>
                        <td>CVC
                            <input class='input-field'></input>
                        </td>
                    </tr>
                </table>
                <div class="button-checkout-field">
                    <input type="submit" class='pay-btn' value="Inoltra il pagamento">
                    <a href="${pageContext.request.contextPath}/shop?action=viewCart" class='pay-btn'>Annulla</a>
                </div>
            </div>

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
