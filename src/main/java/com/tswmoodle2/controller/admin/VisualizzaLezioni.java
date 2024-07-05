package com.tswmoodle2.controller.admin;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CartDaoImpl;

import java.io.IOException;

@WebServlet(name = "VisualizzaLezioni", value = "/VisualizzaLezioni")
public class VisualizzaLezioni extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id=request.getParameter("idCorsoV");
        request.setAttribute("elemento", id);
        /*String courseId=request.getParameter("productId");
        if(courseId!=null) {
            CartDaoImpl c=new CartDaoImpl();
                c.deleteFromCarrello(c.getCartIDByUser(Integer.parseInt(userId)), Integer.parseInt(userId), Integer.parseInt(courseId));
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/admin/modificaCarrello.jsp");
            rd.forward(request, response);
        }*/
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/admin/modificaCorsi.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
