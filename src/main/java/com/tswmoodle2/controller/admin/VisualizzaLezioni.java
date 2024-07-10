package com.tswmoodle2.controller.admin;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Lezione;
import model.dao.LezioneDaoImpl;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "VisualizzaLezioni", value = "/VisualizzaLezioni")
public class VisualizzaLezioni extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id=request.getParameter("idCorsoV");
        String idLezione=request.getParameter("idLezione");
        LezioneDaoImpl l = new LezioneDaoImpl();
        List<Lezione> lezioni = l.findAllByCorsoId(Integer.parseInt(id));
        request.setAttribute("elemento", lezioni);
        if(idLezione!=null) {
            l.delete(Integer.parseInt(idLezione));
        }
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/admin/modificaCorsi.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
