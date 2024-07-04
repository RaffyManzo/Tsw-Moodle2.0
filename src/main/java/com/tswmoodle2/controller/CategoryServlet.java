package com.tswmoodle2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Categoria;
import model.beans.Corso;
import model.beans.Utenza;
import model.dao.CartDaoImpl;
import model.dao.CategoriaDaoImpl;
import model.dao.CorsoDaoImpl;
import model.dao.UtenzaDaoImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CategoryServlet", value = "/category")
public class CategoryServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String parameter = request.getParameter("c");

        if (parameter != null) { // se il parametro c'é selezioniamo una categoria in particolare, altrimenti tutte
            try {
                Categoria categoria = new CategoriaDaoImpl().findByNome(parameter).get(0);

                request.setAttribute("category", categoria);
                request.setAttribute("courseNumber",
                        new CategoriaDaoImpl().getNumberOfCourseCategory(categoria.getNome()));
                request.setAttribute("categoryteacher",
                        new CategoriaDaoImpl().getTeacherWithHigherNumberInCategory(categoria.getNome()));

                pagination(request, response);

                Utenza user = (Utenza) request.getSession().getAttribute("user");
                if(user != null)
                    request.setAttribute("cartID", new CartDaoImpl().getCartIDByUser(user.getIdUtente()));

                request.getRequestDispatcher("/WEB-INF/results/public/category.jsp").forward(request, response);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
        private void pagination(HttpServletRequest request, HttpServletResponse response) {
            String parameter = request.getParameter("c");
            String pageParam = request.getParameter("page");
            int page = (pageParam != null) ? Integer.parseInt(pageParam) : 1;
            int itemsPerPage = 2; // Numero di corsi per pagina

            try {
                List<Corso> corsi;
                int totalCourses;

                if (parameter != null) {
                    Categoria categoria = new CategoriaDaoImpl().findByNome(parameter).get(0);
                    corsi = new CorsoDaoImpl().findByCategoryPaginated(categoria.getNome(), page, itemsPerPage);
                    totalCourses = new CorsoDaoImpl().countByCategory(categoria.getNome());
                    request.setAttribute("category", categoria);
                } else {
                    corsi = new CorsoDaoImpl().findAllPaginated(page, itemsPerPage);
                    totalCourses = new CorsoDaoImpl().countAll();
                }

                request.setAttribute("corsi", corsi);
                request.setAttribute("totalCourses", totalCourses);
                request.setAttribute("currentPage", page);
                request.setAttribute("itemsPerPage", itemsPerPage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


}
