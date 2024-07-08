package com.tswmoodle2.controller.admin;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Corso;
import model.dao.CorsoDaoImpl;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "VisualizzaOrdine", value = "/VisualizzaOrdine")
public class VisualizzaOrdine extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id=request.getParameter("id");
        request.setAttribute("elemento", id);
        CorsoDaoImpl o=new CorsoDaoImpl();
        List<Corso> data =o.getCorsiOrdine(Integer.parseInt(id));
        request.setAttribute("data", data);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/admin/visualizzaOrdini.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
