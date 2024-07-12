package com.tswmoodle2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Utenza;
import model.dao.OrdineDaoImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/orders")
@MultipartConfig
public class DisplayOrdersList extends HttpServlet {

    private static final String ERROR_VIEW = "/WEB-INF/results/public/error.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession().getAttribute("user") != null) {
            Utenza user = (Utenza) request.getSession().getAttribute("user");

            request.setAttribute("orders",new OrdineDaoImpl().findByUtenteId(user.getIdUtente()));

            request.getRequestDispatcher("/WEB-INF/results/private/orders.jsp").forward(request, response);
        } else {
            List<String> errors = new ArrayList<>();
            errors.add("Errore generico");
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(ERROR_VIEW).forward(request, response);
        }
    }
}