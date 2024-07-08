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
import java.util.List;

@WebServlet(name = "AdminSearchFilter", value = "/adminSearchFilter")
public class AdminSearchFilter extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String table = request.getParameter("table");
        List<?> filteredData = null;

        switch (table) {
            case "utenza":
                String tipo = request.getParameter("tipoUtente");
                String emailUtente = request.getParameter("emailUtente");
                UtenzaDaoImpl u = new UtenzaDaoImpl();
                filteredData = u.findByTipoEmail(tipo, emailUtente);
                break;
            case "corso":
                String nomeCategoria = request.getParameter("nomeCategoria");
                String nomeCorso = request.getParameter("nomeCorso");
                CorsoDaoImpl c = new CorsoDaoImpl();
                filteredData = c.findByCategoriaCorso(nomeCategoria, nomeCorso);
                break;
            case "categoria":
                String nomeCategoriaFiltro = request.getParameter("nomeCat");
                CategoriaDaoImpl cat=new CategoriaDaoImpl();
                filteredData = cat.findByNome(nomeCategoriaFiltro);
                break;
            case "ordine":
                String ordineIdUtenteS =request.getParameter("IDUtente");
                String ordineIdS = request.getParameter("IDordine");
                int ordineIdUtente=0;
                int ordineId=0;
                if(!ordineIdS.isEmpty())
                    ordineId = Integer.parseInt(request.getParameter("IDordine"));
                if(!ordineIdUtenteS.isEmpty())
                    ordineIdUtente =Integer.parseInt(request.getParameter("IDUtente"));
                OrdineDaoImpl o = new OrdineDaoImpl();
                filteredData = o.findByUtenteId(ordineIdUtente, ordineId);
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
}
