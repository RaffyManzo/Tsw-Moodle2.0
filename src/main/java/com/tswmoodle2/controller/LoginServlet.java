package com.tswmoodle2.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Util.CarrelloService;
import model.beans.Carrello;
import model.beans.Corso;
import model.beans.Utenza;
import model.dao.CartDaoImpl;
import model.dao.UtenzaDaoImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    RequestDispatcher errorDispatcher;

    public static boolean isEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    @Override
    public void init() {
        errorDispatcher = getServletContext().getRequestDispatcher("/WEB-INF/results/public/error.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println(username);
        Utenza user;
        UtenzaDaoImpl utenza = new UtenzaDaoImpl();
        ArrayList<String> errors = new ArrayList<>();
        if (username == null || username.trim().isEmpty()) {
            errors.add("Il campo username non può essere vuoto!");
        }
        if (password == null || password.trim().isEmpty()) {
            errors.add("Il campo password non può essere vuoto!");
        }

        errorOccurs(errors, request, response);

        username = username.trim();
        password = password.trim();
        String hashPassword = toHash(password);

        System.out.println(isEmail(username));

        if (isEmail(username))
            user = utenza.findByEmail(username);
        else
            user = utenza.findByUsername(username);


        if (user != null) {
            if (hashPassword.equals(user.getPassword())) {
                HttpSession session = request.getSession();

                // Sincronizzo il carrello solo se l'utente é uno studente
                if(user.getTipo().equals("S")) {

                    Map<Corso, Integer> sessionCart = (Map<Corso, Integer>) session.getAttribute("cart");

                    Carrello carrello = new CarrelloService().getOrCreateCarrello(user);

                    if(carrello == null) {
                        errorOccurs(new ArrayList<>(List.of("C'é stato un problema con la generazione del carrello")), request, response);

                        return;
                    } else {

                        if (carrello.getIDCarrello() == -1) {
                            carrello.setIDCarrello(new CartDaoImpl().getCartIDByUser(user.getIdUtente()));
                            carrello.setIDUtente(user.getIdUtente());
                            carrello.setCart(new HashMap<>());
                        }

                    }


                    if (sessionCart != null) {
                        carrello.setCart(sessionCart);
                        new CarrelloService().saveCarrello(carrello);
                    }

                    

                    LOGGER.log(Level.INFO, "Il carrello appena creato ha (IDCarrello-IDUtente): {0}-{1}",
                            List.of(carrello.getIDCarrello(), carrello.getIDUtente()));

                    if (sessionCart != null) {
                        carrello.setCart(sessionCart);
                        new CarrelloService().saveCarrello(carrello);
                    }


                    session.setAttribute("cart", carrello.getCart());
                }
                session.setAttribute("user", user);
                session.setAttribute("isAdmin", user.getTipo().contentEquals("A") ? Boolean.TRUE : Boolean.FALSE);


                if (user.getTipo().equals("A")) {
                    response.sendRedirect("admin");
                    return;
                } else {
                    // Reindirizzamento alla home
                    response.sendRedirect("home");
                }
                return;
            } else {
                errors.add("Username e password non corrispondono");
            }
        } else {
            errors.add("Utenza non trovata");
        }

        errorOccurs(errors, request, response);
    }


    public void errorOccurs(ArrayList<String> errors, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //restituisce gli errori
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            errorDispatcher.forward(request, response);
        }
    }

    private String toHash(String password) {
        StringBuilder hashString = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            for (byte b : hash) {
                hashString.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e);
        }
        return hashString.toString();
    }
}