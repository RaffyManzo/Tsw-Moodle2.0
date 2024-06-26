package com.tswmoodle2.controller;

import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import model.beans.Corso;
import model.dao.CorsoDaoImpl;
import org.json.simple.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "GetCourseInfoServlet", value = "/getCoursesJson")
public class GetCourseInfoServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // Aggiungi questo per consentire le richieste CORS
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");


        ArrayList<Corso> courses = new CorsoDaoImpl().getAllCourses();

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
