package com.tswmoodle2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Corso;
import model.beans.Utenza;
import model.dao.CategoriaDaoImpl;
import model.dao.CorsoDaoImpl;
import model.dao.UtenzaDaoImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "GetCourseInfoServlet", value = "/getCoursesJson")
public class GetCourseInfoServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(GetCourseInfoServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {


        String action = request.getParameter("action");
        if (action == null) {
            action = "all";
        }

        switch (action) {
            case "byCreator":
                getByCreator(response, request);
                break;
            case "yourCategory":
                getByCategory(request, response);
                break;
            default:
                getAll(response);
                break;
        }
    }

    private void getByCategory(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            Utenza user = (Utenza) request.getSession().getAttribute("user");
            ArrayList<Corso> courses;
            if(user != null) {
                String param = new UtenzaDaoImpl().getFavouriteCategory(user.getIdUtente());

                if (param == null) {
                    List<String> errors = new ArrayList<>();
                    errors.add("Errore generico");
                    request.setAttribute("errors", errors);
                    request.getRequestDispatcher("/WEB-INF/results/public/error.jsp").forward(request, response);
                }
                courses = new CorsoDaoImpl().findByCategoria(
                        param
                );
                LOGGER.log(Level.INFO, "Carico i corsi della categoria dell'utente {0}", param);
            } else {
                String mostPurchased = new CategoriaDaoImpl().getMostPurchasedCategory();

                courses = mostPurchased != null ? new CorsoDaoImpl().findByCategoria(
                            mostPurchased) : new CorsoDaoImpl().getAllCourses();

                LOGGER.log(Level.INFO, "Carico i corsi della categoria {0}", mostPurchased);
            }


            sendResponse(response, courses);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private void getByCreator(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
        Integer param = (Integer) request.getSession().getAttribute("c");
        request.getSession().removeAttribute("c");
        if (param == null) {
            List<String> errors = new ArrayList<>();
            errors.add("Errore generico");
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/WEB-INF/results/public/error.jsp").forward(request, response);
        } else {

            ArrayList<Corso> courses = new CorsoDaoImpl().findByCreatore(
                    param
            );
            sendResponse(response, courses);
        }
    }

    private void getAll(HttpServletResponse response) throws IOException {
        ArrayList<Corso> courses = new CorsoDaoImpl().getAllCourses();
        sendResponse(response, courses);
    }


    private void sendResponse(HttpServletResponse response, ArrayList<Corso> courses) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // Aggiungi questo per consentire le richieste CORS
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");



        JSONArray coursesArray = new JSONArray();

        for (Corso course : courses) {
            JSONObject courseObject = new JSONObject();
            courseObject.put("id", course.getIdCorso());
            courseObject.put("category", course.getNomeCategoria());
            courseObject.put("name", course.getNome());
            courseObject.put("description", course.getDescrizione());
            courseObject.put("image", course.getImmagine());
            courseObject.put("certification", course.getCertificazione());
            courseObject.put("creationDate", course.getDataCreazione().toString());
            courseObject.put("price", course.getPrezzo());
            // Usa il metodo toJSON per serializzare l'oggetto creator
            courseObject.put("creator", course.getCreatore().toJSON());
            courseObject.put("numberPurchases", course.getNumeroAcquisti());
            coursesArray.add(courseObject);
        }

        response.getWriter().write(coursesArray.toJSONString());
    }
}
