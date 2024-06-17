package com.tswmoodle2.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Utenza;
import model.dao.UtenzaDaoImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    RequestDispatcher errorDispatcher;
    @Override
    public void init() {
        errorDispatcher = getServletContext().getRequestDispatcher("/WEB-INF/results/NotFound.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println(username);
        Utenza user;
        UtenzaDaoImpl utenza = new UtenzaDaoImpl();
        List<String> errors = new ArrayList<>();
        if (username == null || username.trim().isEmpty()) {
            errors.add("Il campo username non può essere vuoto!");
        }
        if (password == null || password.trim().isEmpty()) {
            errors.add("Il campo password non può essere vuoto!");
        }
        //restituisce gli errori
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            errorDispatcher.forward(request, response);
            return;
        }

        username = username.trim();
        password = password.trim();
        String hashPassword = toHash(password);


        if (username.contains("@"))
            user = utenza.findByEmail(username);
        else
            user = utenza.findByUsername(username);
        if (user != null && hashPassword.equals(user.getPassword())) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("HomeServlet");
            request.getSession().setAttribute("Tipo", user.getTipo()); // inserisco il token nella sessione

            dispatcher.forward(request, response);

        } else {
            errors.add("Username o password non validi!");
            request.setAttribute("errors", errors);
            errorDispatcher.forward(request, response);
        }

    }


    private String toHash(String password) {
        String hashString = null;
        try {
            java.security.MessageDigest digest
                    =
                    java.security.MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            hashString = "";
            for (byte b : hash) {
                hashString += Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3);
            }
        } catch (java.security.NoSuchAlgorithmException e) {
            System.err.println(e);
        }
        return hashString;
    }
}