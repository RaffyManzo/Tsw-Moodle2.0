package com.tswmoodle2.controller.admin;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Argomento;
import model.dao.ArgomentoDaoImpl;

import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "VisualizzaFile", value = "/VisualizzaFile")
public class VisualizzaFile extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idArgomento=request.getParameter("idArgomento");
        String idLezione=request.getParameter("idLezione");
        ArgomentoDaoImpl a = new ArgomentoDaoImpl();
        List<String> files=a.findFiles(Integer.parseInt(idArgomento));
        String id=request.getParameter("idCorsoV");
        request.setAttribute("corso", id);
        request.setAttribute("idLezione", idLezione);
        request.setAttribute("idArgomento", idArgomento);

        String type=request.getParameter("type");
        System.out.println(type + "wew\n");

        for (String file : files) {
            System.out.println(file + "uwu\n");
        }

        if(type!=null){
            files.remove(type);
        }
        a.deleteFile(type, Integer.parseInt(idArgomento));
        request.setAttribute("elemento", files);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/admin/modificaFiles.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
