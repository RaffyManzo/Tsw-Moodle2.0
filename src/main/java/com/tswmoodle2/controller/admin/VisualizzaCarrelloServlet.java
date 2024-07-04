package com.tswmoodle2.controller.admin;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Carrello;
import model.dao.CartDaoImpl;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "VisualizzaCarrelloServlet", value = "/VisualizzaCarrelloServlet")
public class VisualizzaCarrelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId=request.getParameter("id");
        request.setAttribute("elemento", userId);
        String courseId=request.getParameter("productId");
        if(courseId!=null) {
            CartDaoImpl c=new CartDaoImpl();
                c.deleteFromCarrello(c.getCartIDByUser(Integer.parseInt(userId)), Integer.parseInt(userId), Integer.parseInt(courseId));
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/admin/modificaCarrello.jsp");
            rd.forward(request, response);
        }
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/admin/modificaCarrello.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
