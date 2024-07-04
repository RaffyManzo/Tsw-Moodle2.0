package com.tswmoodle2.controller.admin;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "VisualizzaCarrelloServlet", value = "/VisualizzaCarrelloServlet")
public class VisualizzaCarrelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String elemento=request.getParameter("id");
        request.setAttribute("elemento", elemento);
System.out.println("visual carrello servlet");
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/admin/modificaCarrello.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
