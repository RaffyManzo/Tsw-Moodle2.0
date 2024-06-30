package com.tswmoodle2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.beans.Categoria;
import model.beans.Corso;
import model.beans.Utenza;
import model.dao.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SearchServlet", value = "/SearchServlet")
public class SearchServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        System.out.println(searchTerm); //controllo

        List<String> results = new ArrayList<>();
        String searchPattern = "%" + searchTerm + "%";

        CorsoDaoImpl corso=new CorsoDaoImpl();
        List<Corso> corsi=new ArrayList<>(corso.searchByName(searchPattern));

        if(corsi.isEmpty()){
            System.out.println("no corsi");//controllo
            UtenzaDaoImpl utente=new UtenzaDaoImpl();
            List<Utenza> utenti=new ArrayList<>(utente.findByTipoEDati("D", searchTerm));
            if(utenti.isEmpty()){
                System.out.println("no utenti"); //controllo
                CategoriaDaoImpl categoria=new CategoriaDaoImpl();
                Categoria c=categoria.findByNome(searchPattern);
                results.add(c.getNome());
            }else{
                for(Utenza c:utenti){
                    results.add(c.getNome() + " " + c.getCognome());
                }
            }
        }else{
            for(Corso c:corsi){
                results.add(c.getNome() + " " + c.getNomeCategoria() + " " + c.getDescrizione());
            }
        }
        // Costruisci manualmente la stringa JSON
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < results.size(); i++) {
            json.append("\"").append(results.get(i)).append("\"");
            if (i < results.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");

        // Imposta la risposta come JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
