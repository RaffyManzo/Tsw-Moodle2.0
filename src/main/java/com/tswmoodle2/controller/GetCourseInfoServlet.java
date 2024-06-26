package com.tswmoodle2.controller;

import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import model.beans.Corso;
import model.dao.CorsoDaoImpl;
import org.json.simple.*;

import java.io.IOException;
import java.net.Inet4Address;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

@WebServlet(name = "GetCourseInfoServlet", value = "/getCoursesJson")
public class GetCourseInfoServlet extends HttpServlet {
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
            default:
                getAll(response);
                break;
        }
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
            coursesArray.add(courseObject);
        }

        response.getWriter().write(coursesArray.toJSONString());
    }
}
