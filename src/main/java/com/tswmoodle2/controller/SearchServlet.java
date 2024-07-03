package com.tswmoodle2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Categoria;
import model.beans.Corso;
import model.beans.Utenza;
import model.dao.CategoriaDaoImpl;
import model.dao.CorsoDaoImpl;
import model.dao.UtenzaDaoImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchServlet", value = "/search")
public class SearchServlet extends HttpServlet {
    private static final int DISPLAY_LIMIT = 5;
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Imposta gli header per il CORS
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        // Leggi il parametro di ricerca dalla richiesta
        String searchTerm = request.getParameter("q");

        // Esegui la query sui tre diversi tipi di oggetti
        List<Utenza> utenti = new UtenzaDaoImpl().searchByCognomeOrUsernameLimited(searchTerm,DISPLAY_LIMIT );
        List<Corso> corsi = new CorsoDaoImpl().searchByNameLimited(searchTerm, DISPLAY_LIMIT);
        List<Categoria> categorie = new CategoriaDaoImpl().findByNomeLimited(searchTerm, DISPLAY_LIMIT);

        // Crea il JSON di risposta
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("utenti", createUtentiArray(utenti));
        jsonResponse.put("corsi", createCorsiArray(corsi));
        jsonResponse.put("categorie", createCategorieArray(categorie));

        // Scrivi la risposta
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toJSONString());
    }

    private JSONArray createUtentiArray(List<Utenza> utenti) {
        JSONArray utentiArray = new JSONArray();
        for (Utenza utente : utenti) {
            JSONObject utenteObject = new JSONObject();
            utenteObject.put("id", utente.getIdUtente());

            utenteObject.put("surname", utente.getCognome());
            utenteObject.put("name", utente.getNome());
            utenteObject.put("image", utente.getImg());
            utenteObject.put("corsi", new UtenzaDaoImpl().getNumeroCorsi(utente.getIdUtente()));

            // aggiungi altri campi necessari
            utentiArray.add(utenteObject);
        }
        return utentiArray;
    }

    private JSONArray createCorsiArray(List<Corso> corsi) {
        JSONArray corsiArray = new JSONArray();
        for (Corso corso : corsi) {
            JSONObject corsoObject = new JSONObject();

            corsoObject.put("id", corso.getIdCorso());
            corsoObject.put("category", corso.getNomeCategoria());
            corsoObject.put("name", corso.getNome());
            corsoObject.put("description", corso.getDescrizione());
            corsoObject.put("image", corso.getImmagine());
            corsoObject.put("price", corso.getPrezzo());
            corsoObject.put("creator", corso.getCreatore().toJSON());
            corsoObject.put("numberPurchases", corso.getNumeroAcquisti());

            corsiArray.add(corsoObject);
        }
        return corsiArray;
    }

    private JSONArray createCategorieArray(List<Categoria> categorie) {
        JSONArray categorieArray = new JSONArray();
        for (Categoria categoria : categorie) {
            JSONObject categoriaObject = new JSONObject();

            categoriaObject.put("name", categoria.getNome());

            // aggiungi altri campi necessari
            categorieArray.add(categoriaObject);
        }
        return categorieArray;
    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
