package com.tswmoodle2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Utenza;
import model.dao.CartDaoImpl;
import model.dao.CategoriaDaoImpl;
import model.dao.UtenzaDaoImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "TeacherProfileServlet", urlPatterns = "/profile")
public class TeacherProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String profileID = request.getParameter("id");

        try {
            Utenza profile = new UtenzaDaoImpl().findByID(Integer.parseInt(profileID));
            if(!profile.getTipo().equals("D")) {
                errorOccurs(request, response);
            }

            request.setAttribute("profile", profile);
            request.setAttribute("corsipercategoria", new UtenzaDaoImpl().getCountCourseCategory(profile.getIdUtente()));
            request.setAttribute("countcourse", new UtenzaDaoImpl().getNumeroCorsi(profile.getIdUtente()));
            request.getSession().setAttribute("c", Integer.parseInt(profileID));

            Utenza user = (Utenza) request.getSession().getAttribute("user");
            if(user != null)
                request.setAttribute("cartID", new CartDaoImpl().getCartIDByUser(user.getIdUtente()));

            request.setAttribute("categories", new CategoriaDaoImpl().getAllCategorie());


            request.getRequestDispatcher("/WEB-INF/results/public/profile.jsp").forward(request, response);

        } catch(Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void errorOccurs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        errors.add("L'utente che stai cercando non é un docente");
        errors.add("Non puoi accedere ai profili privati degli utenti!");
        request.setAttribute("errors", errors);
        request.getRequestDispatcher("/WEB-INF/results/public/error.jsp").forward(request, response);

    }
}
