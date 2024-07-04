package com.tswmoodle2.controller.admin;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.beans.Categoria;
import model.beans.Corso;
import model.beans.Utenza;
import model.dao.CategoriaDaoImpl;
import model.dao.CorsoDaoImpl;
import model.dao.UtenzaDaoImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "AdminSearchFilter", value = "/adminSearchFilter")
public class AdminSearchFilter extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String table = request.getParameter("table");
        List<?> filteredData = null;

        switch (table) {
            case "utenza":
                String tipo = request.getParameter("tipoUtente");
                UtenzaDaoImpl u = new UtenzaDaoImpl();
                filteredData = u.findByTipo(tipo);
                break;
            case "corso":
                String nomeCategoria = request.getParameter("nomeCategoria");
                CorsoDaoImpl c = new CorsoDaoImpl();
                filteredData = c.findByCategoria(nomeCategoria);
                break;
            case "categoria":
                String nomeCategoriaFiltro = request.getParameter("nome");
                CategoriaDaoImpl cat=new CategoriaDaoImpl();
                filteredData = cat.findByNome(nomeCategoriaFiltro);
                break;
            default:
                System.out.println("Errore");
                break;
        }

        request.setAttribute("table", table);
        request.setAttribute("data", filteredData);
        request.getRequestDispatcher("/WEB-INF/results/admin/admin.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private List<Utenza> filterUtenza(String username, String tipo) {
        // Implement your database filtering logic here
        // Example:
        // return utenzaDao.findByUsernameAndTipo(username, tipo);
        return null;
    }

    private List<Corso> filterCorso(String nomeCorso, String nomeCategoria) {
        // Implement your database filtering logic here
        // Example:
        // return corsoDao.findByNomeAndCategoria(nomeCorso, nomeCategoria);
        return null;
    }

    private List<Categoria> filterCategoria(String nomeCategoria) {
        // Implement your database filtering logic here
        // Example:
        // return categoriaDao.findByNome(nomeCategoria);
        return null;
    }
}
