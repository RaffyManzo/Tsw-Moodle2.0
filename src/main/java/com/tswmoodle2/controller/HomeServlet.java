package com.tswmoodle2.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.beans.Utenza;
import model.dao.CartDaoImpl;
import model.dao.CorsoDaoImpl;

import java.io.IOException;

@WebServlet(name = "HomeServlet", value = "/home")
public class HomeServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utenza user = (Utenza) request.getSession().getAttribute("user");
        if(user != null)
            request.setAttribute("cartID", new CartDaoImpl().getCartIDByUser(user.getIdUtente()));

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/public/home.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
