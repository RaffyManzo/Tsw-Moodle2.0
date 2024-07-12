package com.tswmoodle2.controller;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Util.CarrelloService;
import model.beans.Carrello;
import model.beans.Corso;
import model.beans.CreditCard;
import model.beans.Utenza;
import model.dao.CartDaoImpl;
import model.dao.CreditCardDaoImpl;
import model.dao.OrdineDaoImpl;
import model.dao.UtenzaDaoImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@WebServlet(name = "CheckoutServlet", value = "/checkout")
public class CheckoutServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utenza user = ((Utenza)request.getSession().getAttribute("user"));
        if(user != null) {
            if (user.getTipo().equals("S")) {
                String action = request.getParameter("a");

                if (!checkAccount(request)) {
                    error(request, response, "Utente non autenticato");
                    return;
                }

                switch (Objects.requireNonNull(action)) {
                    case "redirect":
                        redirectToCheckoutPage(request, response);
                        break;
                    case "purchase":
                        purchaseCourses(request, response);
                        break;
                    default:
                        error(request, response, "Parametro non valido");
                        break;
                }

            } else {
                error(request, response, "Il tuo account non supporta questa funzionalitá, prova a creare un account studente");
            }
        } else {
            error(request, response, "Funzionalitá riservata, effettua il login");
        }
    }

    private void purchaseCourses(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession(false);
        Utenza user = ((Utenza)session.getAttribute("user"));
        Carrello cart = new CartDaoImpl().getCartByUserID(user.getIdUtente());
        UtenzaDaoImpl utenzaDao = new UtenzaDaoImpl();
        int cartItem = (Integer)session.getAttribute("cartItemCount");


        try {
            CreditCard card = checkCardValidity(request);
            if (card != null) {

                session.removeAttribute("cart");
                session.removeAttribute("cartItemCount");


                new OrdineDaoImpl().purchaseCoursesFromCart(cart, card);
                success(request, response, cart);
            } else {
                error(request, response, "Metodo di pagamento non valido");
            }
        } catch (SQLException e) {
            String message = e.getMessage();
            if (message.contains("Il corso con ID")) {

                error(request, response, e.getMessage());
            } else {
                throw new ServletException(e);
            }
        }
    }

    private CreditCard checkCardValidity( HttpServletRequest request) {
        String cardNumber = request.getParameter("cardNumber");
        String cardHolder = request.getParameter("cardHolder");
        String expiryDate = request.getParameter("expiryDate");
        String cvc = request.getParameter("cvc");

        if( isValidCardNumber(cardNumber) && isValidExpiryDate(expiryDate) && isValidCVC(cvc)) {
            CreditCard creditCard = new CreditCard(cardNumber,
                    cardHolder, expiryDate,
                    ((Utenza)request.getSession().getAttribute("user")).getIdUtente());

            if(new CreditCardDaoImpl().findByNumber(creditCard.getNumeroCarta()) == null) {
                new CreditCardDaoImpl().saveCard(creditCard);
            }
            return creditCard;
        } else return null;
    }

    private boolean isValidCardNumber(String cardNumber) {
        // Implementa la logica di validazione del numero della carta di credito
        return cardNumber != null && cardNumber.matches("\\d{4} \\d{4} \\d{4} \\d{4}");    }

    private boolean isValidExpiryDate(String expiryDate) {
        // Implementa la logica di validazione della data di scadenza
        if (expiryDate != null && expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            String[] parts = expiryDate.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt("20" + parts[1]); // Assumi che l'anno sia nel formato 'YY'
            LocalDate now = LocalDate.now();
            LocalDate expiry = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());
            return !expiry.isBefore(now);
        }
        return false;
    }

    private boolean isValidCVC(String cvc) {
        // Implementa la logica di validazione del CVC
        return cvc != null && cvc.matches("\\d{3}");
    }


    boolean checkAccount(HttpServletRequest request) {
        HttpSession session = request.getSession();

        if(session != null) {
            return session.getAttribute("user") != null;
        } else {
            return false;
        }
    }

    private void error(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        errors.add(error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher("/WEB-INF/results/public/error.jsp").forward(request, response);
    }

    private void success(HttpServletRequest request, HttpServletResponse response, Carrello cart) throws ServletException, IOException {
        request.setAttribute("operation", "pagamento");
        request.setAttribute("message-header", "Congratulazioni, pagamento riuscito");
        List<String> msgs = new ArrayList<>();

        for (Corso c : cart.getCart().keySet()) {
            msgs.add(c.getNome() + " é stato aggiunto al tuo account");
        }
        request.setAttribute("messages", msgs);

        request.getRequestDispatcher("/WEB-INF/results/public/success.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    protected Carrello saveCartIntoDb(HttpServletRequest request) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Utenza user = (Utenza) session.getAttribute("user");

            if (user != null) {
                CarrelloService carrelloService = new CarrelloService();
                Carrello carrello = carrelloService.getOrCreateCarrello(user);

                if (carrello.getIDCarrello() == -1) {
                    carrello.setIDCarrello(new CartDaoImpl().getCartIDByUser(user.getIdUtente()));
                    carrello.setIDUtente(user.getIdUtente());
                    carrello.setCart(new HashMap<>());
                }

                carrello.setCart((Map<Corso, Integer>) session.getAttribute("cart"));

                CartDaoImpl cartDao = new CartDaoImpl();
                cartDao.saveOrUpdateCarrello(carrello);

                // Aggiorna la sessione con il carrello aggiornato dal database
                session.setAttribute("cart", carrello.getCart());

                return carrello;
            }
        }

        return null;
    }

    private void redirectToCheckoutPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Map<Corso, Integer> cart = (Map<Corso, Integer>) request.getSession(false).getAttribute("cart");


        if(cart == null || cart.isEmpty()) {
            // Usa l'header Referer per determinare la pagina di origine
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer);
        } else {

            Carrello dbCart = saveCartIntoDb(request);
            request.setAttribute("cart", dbCart);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/private/checkout.jsp");
            rd.forward(request, response);
        }
    }

}
