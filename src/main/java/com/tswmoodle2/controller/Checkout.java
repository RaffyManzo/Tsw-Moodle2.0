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

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet(name = "Checkout", value = "/checkout")
public class Checkout  extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



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


}
