package com.tswmoodle2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Util.CarrelloScheduler;
import model.Util.CarrelloService;
import model.beans.Carrello;
import model.beans.Corso;
import model.beans.Utenza;
import model.dao.CartDaoImpl;
import model.dao.CorsoDaoImpl;
import model.dao.UtenzaDaoImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "TeacherProfileServlet", urlPatterns = "/profile")
public class TeacherProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userID = request.getParameter("id");

        try {
            Utenza user = new UtenzaDaoImpl().findByID(Integer.parseInt(userID));
            if(!user.getTipo().equals("D")) {
                errorOccurs(request, response);
            }

            request.setAttribute("profile", user);
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
