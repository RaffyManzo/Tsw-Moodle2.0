package com.tswmoodle2.controller.admin;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Argomento;
import model.dao.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "VisualizzaArgomenti", value = "/VisualizzaArgomenti")
public class VisualizzaArgomenti extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type=request.getParameter("type");
        String id=request.getParameter("idCorsoV");
        request.setAttribute("corso", id);
        String idLezione=request.getParameter("idLezione");
        ArgomentoDaoImpl a = new ArgomentoDaoImpl();
        List<Argomento> argomenti=a.findAllByLezioneId(Integer.parseInt(idLezione));
        request.setAttribute("elemento", argomenti);
        request.setAttribute("idLezione", idLezione);

        switch (type) {
            case "view":
                break;
            case "delete":
                String idArgomento=request.getParameter("idArgomento");
                a.delete(Integer.parseInt(idArgomento));
                break;
            default:
                throw new IllegalArgumentException("Invalid action");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/admin/modificaArgomenti.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
