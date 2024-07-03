package com.tswmoodle2.controller.admin;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CategoriaDaoImpl;
import model.dao.CorsoDaoImpl;
import model.dao.UtenzaDaoImpl;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ModificaServlet", value = "/ModificaServlet")
public class ModificaServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tipo = request.getParameter("tipo");
        String elemento=request.getParameter("id");
        request.setAttribute("tipo", tipo);
        request.setAttribute("elemento", elemento);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/admin/modificaAdmin.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
