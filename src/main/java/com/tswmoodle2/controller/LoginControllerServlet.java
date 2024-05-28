package com.tswmoodle2.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LoginControllerServlet", value = "/check")
public class LoginControllerServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) { // Se ci sono cookie verifica se esiste un cookie con il nome "username"
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    if(!cookie.getValue().isEmpty()) {
                        request.setAttribute("username", cookie.getValue());
                        RequestDispatcher rd = request.getRequestDispatcher("login.html");
                        rd.forward(request, response);
                    }
                }
            }
        }

        //Utente non trovato nei cookie, registrazione
        RequestDispatcher rd = request.getRequestDispatcher("registrazione.html");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}