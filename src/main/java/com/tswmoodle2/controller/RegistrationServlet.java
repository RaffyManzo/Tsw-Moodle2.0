package com.tswmoodle2.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.beans.Utenza;
import model.dao.UtenzaDaoImpl;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

@WebServlet(name = "RegistrationServlet", value = "/new")
public class RegistrationServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("name");
        String cognome = request.getParameter("surname");
        String dataNascitaStr = request.getParameter("birth-date");
        String indirizzo = request.getParameter("address");
        String citta = request.getParameter("citta");
        String telefono = String.valueOf(Integer.parseInt(request.getParameter("phoneNumber")));
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String username = request.getParameter("username");
        String tipo = request.getParameter("type");
        Date dataNascita = null;
        Date dataCreazioneAccount = Date.valueOf(LocalDate.now());

        try {
            dataNascita = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(dataNascitaStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Crea una nuova utenza

        //COME METTIAMO ID UTENTE CHE SI AUTO INCREMENTA?
        Utenza nuovaUtenza = new Utenza(0, nome, cognome, dataNascita, indirizzo, citta, telefono, email, password, dataCreazioneAccount, username, tipo, "");
        UtenzaDaoImpl utenzaDao=new UtenzaDaoImpl();
        // Salva l'utenza nel database
        utenzaDao.insertInto(nuovaUtenza);

        RequestDispatcher dispatcherToLoginPage = request.getRequestDispatcher("login.html");
        dispatcherToLoginPage.forward(request, response);
    }
}