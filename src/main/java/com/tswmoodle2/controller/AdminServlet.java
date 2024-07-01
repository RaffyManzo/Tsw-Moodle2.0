package com.tswmoodle2.controller;

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

@WebServlet(name = "AdminServlet", value = "/AdminServlet")
public class AdminServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String table = request.getParameter("table-select");

        if(table==null) {
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/public/admin.jsp");
            rd.forward(request, response);
        }
        List<?> data = null;
        switch (table) {
            case "utenza":
                UtenzaDaoImpl u=new UtenzaDaoImpl();
                data = u.getAllUsers();
                break;
            case "corso":
                CorsoDaoImpl c=new CorsoDaoImpl();
                data = c.getAllCourses();
                break;
            case "categoria":
                CategoriaDaoImpl ca=new CategoriaDaoImpl();
                data = ca.getCategorie();
                break;
            case "lezione":
                //data = YourDaoImpl.getLezioneData();
                // break;
            default:
                throw new IllegalArgumentException("Invalid table name: " + table);
        }

        request.setAttribute("data", data);
        request.setAttribute("table", table);
        request.getRequestDispatcher("/WEB-INF/results/public/admin.jsp").forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
