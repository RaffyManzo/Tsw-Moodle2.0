package com.tswmoodle2.controller;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.beans.Corso;

import java.io.IOException;
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
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/private/checkout.jsp");
            rd.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
