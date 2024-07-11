package com.tswmoodle2.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

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
    private static final Logger LOGGER = Logger.getLogger(AccountServlet.class.getName());
    private static final String ERROR_VIEW = "/WEB-INF/results/public/error.jsp";
    private static final String SUCCESS_VIEW = "/WEB-INF/results/public/success.jsp";
    private static final String ACCOUNT_VIEW = "/WEB-INF/results/private/account.jsp";
    private static final String MODIFY_PASSWORD_ACTION = "modify-password";
    private static final String DELETE_PARAM = "d";
    private static final String DELETE_TRUE = "TRUE";
    private static final String PHONE_PARAM = "t";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(ACCOUNT_VIEW).forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String delete = request.getParameter(DELETE_PARAM);
        if (DELETE_TRUE.equals(delete)) {
            deleteAccount(request, response);
            return;
        }

        String action = request.getParameter("action");
        if (MODIFY_PASSWORD_ACTION.equals(action)) {
            handlePasswordModification(request, response);
            return;
        }

        String phone = request.getParameter(PHONE_PARAM);
        if (phone != null) {
            updateUserPhone(request, response, phone);
            return;
        }

        request.getRequestDispatcher(ACCOUNT_VIEW).forward(request, response);
    }

    private void handlePasswordModification(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String password = request.getParameter("password");
        String passwordCheck = request.getParameter("password-check");

        ArrayList<String> errors = new ArrayList<>();
        if (!password.equals(passwordCheck)) {
            errors.add("Le password non corrispondono");
            forwardWithError(request, response, errors);
            return;
        }

        if (!isValidPassword(password)) {
            errors.add("Password non rispetta i criteri");
            forwardWithError(request, response, errors);
            return;
        }

        try {
            updatePassword(request, response, password);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating password", e);
            ArrayList<String> err = new ArrayList<>();
            err.add("Errore nell'aggiornamento della password");
            forwardWithError(request, response, err);
        }
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, ArrayList<String> errors) throws ServletException, IOException {
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(ERROR_VIEW).forward(request, response);
    }

    private void updateUserPhone(HttpServletRequest request, HttpServletResponse response, String phone) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utenza user = getUserFromSession(session);
        if (user != null) {
            user.setTelefono(phone);
            new UtenzaDaoImpl().update(user);
            session.setAttribute("user", user);
            request.getRequestDispatcher(ACCOUNT_VIEW).forward(request, response);
        } else {
            ArrayList<String> err = new ArrayList<>();
            err.add("Non hai i permessi per accedere a questa risorsa");
            forwardWithError(request, response, err);
        }
    }

    private void updatePassword(HttpServletRequest request, HttpServletResponse response, String newPassword) throws Exception {
        HttpSession session = request.getSession();
        Utenza user = getUserFromSession(session);
        if (user != null) {
            user.setPassword(hashPassword(newPassword));
            new UtenzaDaoImpl().update(user);
            session.setAttribute("user", user);
            request.getRequestDispatcher(ACCOUNT_VIEW).forward(request, response);
        } else {
            ArrayList<String> err = new ArrayList<>();
            err.add("Non hai i permessi per accedere a questa risorsa");
            forwardWithError(request, response, err);
        }
    }

    private Utenza getUserFromSession(HttpSession session) {
        return (Utenza) session.getAttribute("user");
    }

    private boolean isValidPassword(String password) {
        String uppercasePattern = ".[A-Z].";
        String digitPattern = ".[0-9].";
        String specialCharacterPattern = ".[!@#$%^&(),.?\":{}|<>].*";

        return password.length() >= 8 && password.length() <= 15 &&
                Pattern.matches(uppercasePattern, password) &&
                Pattern.matches(digitPattern, password) &&
                Pattern.matches(specialCharacterPattern, password);
    }

    private String hashPassword(String password) {
        StringBuilder hashString = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            for (byte b : hash) {
                hashString.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "No such algorithm for password hashing", e);
        }
        return hashString.toString();
    }

    private void deleteAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Utenza user = getUserFromSession(session);
            if (user != null) {
                new UtenzaDaoImpl().delete(user.getIdUtente());
                session.invalidate();
                request.setAttribute("operation", "cancellazione account");
                request.setAttribute("message-header", "Addio " + user.getNome());
                List<String> msgs = List.of("Il tuo account é stato eliminato in modo corretto", "I tuoi dati sono stati eliminati");
                request.setAttribute("messages", new ArrayList<>(msgs));
                request.getRequestDispatcher(SUCCESS_VIEW).forward(request, response);
            } else {
                ArrayList<String> err = new ArrayList<>();
                err.add("Non hai i permessi per accedere a questa risorsa");
                forwardWithError(request, response, err);
            }
        } else {
            ArrayList<String> err = new ArrayList<>();
            err.add("Non hai i permessi per accedere a questa risorsa");
            forwardWithError(request, response, err);
        }
    }
}