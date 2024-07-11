package com.tswmoodle2.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CategoriaDaoImpl;
import model.dao.CorsoDaoImpl;
import model.dao.OrdineDaoImpl;
import model.dao.UtenzaDaoImpl;

import java.io.IOException;

@WebServlet(name = "AdminDeleteObjectServlet", value = "/adminDelete")
public class AdminDeleteObjectServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String table = request.getParameter("tipo");
        switch (table) {
            case "utenza":
                UtenzaDaoImpl u = new UtenzaDaoImpl();
                int idu = Integer.parseInt(request.getParameter("id"));
                break;
            case "corso":
                CorsoDaoImpl c = new CorsoDaoImpl();
                int idc = Integer.parseInt(request.getParameter("id"));
                if(c.isDeleted(idc)){
                    c.restore(idc);
                }else{
                    c.delete(idc);
                }
                break;
            case "categoria":
                CategoriaDaoImpl ca = new CategoriaDaoImpl();
                String idca = request.getParameter("id");
                ca.delete(idca);
                break;
            case "ordine":
                OrdineDaoImpl o = new OrdineDaoImpl();
                int ido = Integer.parseInt(request.getParameter("id"));
                o.delete(ido);
                break;
            default:
                throw new IllegalArgumentException("Invalid table name: " + table);
        }
        response.sendRedirect("admin?table-select=" + table);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
