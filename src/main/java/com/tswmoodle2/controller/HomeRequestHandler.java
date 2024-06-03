package com.tswmoodle2.controller;

import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import model.beans.Corso;
import model.dao.CorsoDao;
import model.dao.CorsoDaoImpl;
import org.json.simple.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(name = "HomeRequestHandler", value = "/courses")
public class HomeRequestHandler extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

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
            coursesArray.add(courseObject);
        }

        response.getWriter().write(coursesArray.toJSONString());
    }
}