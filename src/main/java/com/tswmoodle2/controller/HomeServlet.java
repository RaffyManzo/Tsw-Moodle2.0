package com.tswmoodle2.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "HomeServlet", value = "/home")
public class HomeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*Cookie[] cookies = request.getCookies();

        if (cookies != null) { // Se ci sono cookie verifica se esiste un cookie con il nome "username"
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    if(!cookie.getValue().isEmpty()) {
                        request.setAttribute("username", cookie.getValue());
                        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/home.jsp");
                        rd.forward(request, response);
                    }
                }
            }
        }

        //Utente non trovato nei cookie, registrazione */
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/home.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
