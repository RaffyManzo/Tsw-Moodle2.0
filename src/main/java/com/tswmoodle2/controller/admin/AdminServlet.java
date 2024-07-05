package com.tswmoodle2.controller.admin;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.CategoriaDaoImpl;
import model.dao.CorsoDaoImpl;
import model.dao.OrdineDaoImpl;
import model.dao.UtenzaDaoImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "AdminServlet", value = "/admin")
public class AdminServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        if (session != null) {
            if (session.getAttribute("isAdmin").equals(Boolean.TRUE)) {
                serveAdminRequest(request, response);
            } else {
                List<String> errors = new ArrayList<>();
                errors.add("Non hai i permessi per accedere a questa risorsa, prova ad effettuare l'accesso");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/WEB-INF/results/public/error.jsp").forward(request, response);
            }
        }


    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private void serveAdminRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String table = request.getParameter("table-select");

        if (table == null) {
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/admin/admin.jsp");
            rd.forward(request, response);
        }
        List<?> data = null;
        switch (Objects.requireNonNull(table)) {
            case "utenza":
                UtenzaDaoImpl u = new UtenzaDaoImpl();
                data = u.getAllUsers();
                break;
            case "corso":
                CorsoDaoImpl c = new CorsoDaoImpl();
                data = c.getAllCourses();
                break;
            case "categoria":
                CategoriaDaoImpl ca = new CategoriaDaoImpl();
                data = ca.getCategorie();
                break;
            case "ordine":
                OrdineDaoImpl o = new OrdineDaoImpl();
                data=o.getAllOrdini();
                break;
            default:
                throw new IllegalArgumentException("Invalid table name: " + table);
        }

        request.setAttribute("data", data);
        request.setAttribute("table", table);
        request.getRequestDispatcher("/WEB-INF/results/admin/admin.jsp").forward(request, response);

    }
}
