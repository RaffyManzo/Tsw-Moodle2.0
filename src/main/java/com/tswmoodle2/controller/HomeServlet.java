package com.tswmoodle2.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Utenza;
import model.dao.CartDaoImpl;
import model.dao.CategoriaDaoImpl;
import model.dao.CorsoDaoImpl;

import java.io.IOException;

@WebServlet(name = "HomeServlet", value = "/home")
public class HomeServlet extends HttpServlet {

    private static final String TEACHER = "D";
    private static final String ADMIN = "A";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utenza user = (Utenza) request.getSession().getAttribute("user");
        if (user != null) {
            request.setAttribute("cartID", new CartDaoImpl().getCartIDByUser(user.getIdUtente()));

            if (user.getTipo().equals(TEACHER)) {
                request.setAttribute("courses",
                        new CorsoDaoImpl().findByCreatore(user.getIdUtente())
                        );
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/private/teacher/home.jsp");
                rd.forward(request, response);
                return;

            } else if(user.getTipo().equals(ADMIN)) {
                response.sendRedirect(request.getContextPath() + "/admin");
                return;
            }
        }
        request.setAttribute("categories", new CategoriaDaoImpl().getAllCategorie());

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/public/home.jsp");
        rd.forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
