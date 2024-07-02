package com.tswmoodle2.controller;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Util.CarrelloService;
import model.beans.Carrello;
import model.beans.Corso;
import model.beans.Utenza;
import model.dao.CartDaoImpl;
import model.dao.UtenzaDaoImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@WebServlet(name = "CheckoutServlet", value = "/checkout")
public class CheckoutServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("a");

        if(!checkAccount(request)) {
            error(request, response, "Utente non autenticato");
            return;
        }

        switch (Objects.requireNonNull(action)) {
            case "redirect":
                redirectToCheckoutPage(request,response);
                break;
            case "purchase":
                    purchaseCourse(request, response);
                    break;
            default:
                error(request, response, "Parametro non valido");
                break;
        }


    }

    private void purchaseCourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession(false);
        Utenza user = ((Utenza)session.getAttribute("user"));
        Carrello cart = new CartDaoImpl().getCartByUserID(user.getIdUtente());
        UtenzaDaoImpl utenzaDao = new UtenzaDaoImpl();
        CartDaoImpl cartDao = new CartDaoImpl();

        if(checkCardValidity(request)) {
            session.removeAttribute("cart");

            for(Corso c: cart.getCart().keySet()) {
                utenzaDao.purchaseCourse(user.getIdUtente(), c.getIdCorso());
            }

            cartDao.clearCart(cart.getIDCarrello());

        } else {
            error(request, response, "Metodo di pagamento non valido");
            return;
        }
    }

    private boolean checkCardValidity( HttpServletRequest request) {
        return true;
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    protected Carrello saveCartIntoDb(HttpServletRequest request) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Utenza user = (Utenza) session.getAttribute("user");

            if (user != null) {
                Carrello carrello = new CarrelloService().getOrCreateCarrello(user);
                carrello.setCart((Map<Corso, Integer>) session.getAttribute("cart"));

                CartDaoImpl cartDao = new CartDaoImpl();
                cartDao.saveOrUpdateCarrello(carrello);
                session.setAttribute("cart", carrello.getCart()); // replace session attribute with db cart

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
