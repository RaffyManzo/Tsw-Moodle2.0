package com.tswmoodle2.controller.admin;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Corso;
import model.beans.Utenza;
import model.dao.CorsoDaoImpl;
import model.dao.UtenzaDaoImpl;

import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet(name = "ModificaServlet", value = "/ModificaServlet")
public class ModificaServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tipo = request.getParameter("tipo");
        String elemento=request.getParameter("id");
        String nome=request.getParameter("nome");
        request.setAttribute("tipo", tipo);
        request.setAttribute("elemento", elemento);
        if(nome==null) {
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/admin/modificaAdmin.jsp");
            rd.forward(request, response);
        }

        switch (tipo) {
            case "utenza":
                UtenzaDaoImpl u = new UtenzaDaoImpl();
                Utenza utenza= null;
                try {
                    utenza = new Utenza(Integer.parseInt(elemento), nome, request.getParameter("cognome"), parseDate(request.getParameter("dataNascita")),
                            request.getParameter("indirizzo"), request.getParameter("citta"), request.getParameter("telefono"),
                            request.getParameter("email"), request.getParameter("password"), parseDate(request.getParameter("dataCreazioneAccount")),
                            request.getParameter("username"), request.getParameter("tipoUtente"), request.getParameter("immagine"));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                u.update(utenza);
                break;
            case "corso":
                CorsoDaoImpl c = new CorsoDaoImpl();
                UtenzaDaoImpl ut =new UtenzaDaoImpl();
                Corso corso= null;
                try {
                    corso = new Corso(Integer.parseInt(elemento), request.getParameter("categoria"), nome,
                            request.getParameter("descrizione"), request.getParameter("immagine"), request.getParameter("certificazione"),
                            parseDate(request.getParameter("creazione")), ut.findByID(Integer.parseInt(request.getParameter("creatore"))),
                            Double.parseDouble(request.getParameter("prezzo")), Integer.parseInt(request.getParameter("acquisti")));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                c.update(corso);
                break;
        }

        response.sendRedirect("admin?table-select=" + tipo);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date parsedDate = inputFormat.parse(dateString);

        return outputFormat.parse(outputFormat.format(parsedDate));
    }
}
