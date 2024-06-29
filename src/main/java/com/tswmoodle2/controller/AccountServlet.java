package com.tswmoodle2.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.beans.Utenza;
import model.dao.UtenzaDaoImpl;

@WebServlet("/account")
public class AccountServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/results/private/account.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String delete = request.getParameter("d");
        if(delete != null)
            if ("TRUE".equals(delete)) {
                deleteAccount(request, response);
                return;
            }

        String param = request.getParameter("t");
        if (param != null) {
            updateUserPhone(request, response, param);
        }

        request.getRequestDispatcher("/WEB-INF/results/private/account.jsp").forward(request, response);
    }

    private void updateUserPhone(HttpServletRequest request, HttpServletResponse response, String phone) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utenza user = (Utenza) session.getAttribute("user");
        if (user != null) {
            user.setTelefono(phone);
            new UtenzaDaoImpl().update(user);
            session.setAttribute("user", user); // aggiorno l'utenza in sessione
        } else {
            List<String> errors = new ArrayList<>();
            errors.add("Non hai i permessi per accedere a questa risorsa");
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/WEB-INF/results/public/error.jsp").forward(request, response);
        }
    }

    private void deleteAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Utenza user = (Utenza) session.getAttribute("user");
            if (user != null) {
                new UtenzaDaoImpl().delete(user.getIdUtente());
                session.invalidate();
                request.setAttribute("operation", "cancellazione account");
                request.setAttribute("message-header", "Addio " + user.getNome());
                List<String> msgs = new ArrayList<>();
                msgs.add("Il tuo account é stato eliminato in modo corretto");
                msgs.add("I tuoi dati sono stati eliminati");
                request.setAttribute("messages", msgs);

                request.getRequestDispatcher("/WEB-INF/results/public/success.jsp").forward(request, response);
            } else {
                List<String> errors = new ArrayList<>();
                errors.add("Non hai i permessi per accedere a questa risorsa");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/WEB-INF/results/public/error.jsp").forward(request, response);
            }
        } else {
            List<String> errors = new ArrayList<>();
            errors.add("Non hai i permessi per accedere a questa risorsa");
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/WEB-INF/results/public/error.jsp").forward(request, response);
        }
    }
}
