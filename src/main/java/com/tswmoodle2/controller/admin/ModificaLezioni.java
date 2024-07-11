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

@WebServlet(name = "ModificaLezioni", value = "/ModificaLezioni")
public class ModificaLezioni extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idLezione=request.getParameter("idLezione");
        String id=request.getParameter("idCorsoV");
        request.setAttribute("corso", id);
        System.out.println(idLezione);
        LezioneDaoImpl l = new LezioneDaoImpl();
        Lezione lez=l.findById(Integer.parseInt(idLezione));
        request.setAttribute("lezione", lez);

        String titolo=request.getParameter("titolo");
        if(titolo==null){
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/admin/modificaLezioni.jsp");
            rd.forward(request, response);
        }else {
            lez.setDescrizione(request.getParameter("descrizione"));
            lez.setTitolo(titolo);
            l.update(lez);
        }
        List<Lezione> lezioni = l.findAllByCorsoId(Integer.parseInt(id));
        request.setAttribute("elemento", lezioni);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/admin/visualizzaLezioni.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}