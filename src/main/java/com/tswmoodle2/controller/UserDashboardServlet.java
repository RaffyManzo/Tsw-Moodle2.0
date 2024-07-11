package com.tswmoodle2.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Categoria;
import model.beans.Corso;
import model.beans.Utenza;
import model.dao.CartDaoImpl;
import model.dao.CategoriaDaoImpl;
import model.dao.CorsoDaoImpl;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserDashboardServlet", urlPatterns = "/dashboard")

public class UserDashboardServlet extends HttpServlet {
    private static final String DASHBOARD_VIEW = "/WEB-INF/results/private/dashboard.jsp";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utenza user = (Utenza) request.getSession().getAttribute("user");
        if (user != null) {
            request.setAttribute("cartID", new CartDaoImpl().getCartIDByUser(user.getIdUtente()));
            request.setAttribute("courses",
                    new CorsoDaoImpl().getCorsiAcquistati(user.getIdUtente())
            );
        }
        request.setAttribute("categories", new CategoriaDaoImpl().getAllCategorie());


        RequestDispatcher rd = request.getRequestDispatcher(DASHBOARD_VIEW);
        rd.forward(request, response);
    }

}
