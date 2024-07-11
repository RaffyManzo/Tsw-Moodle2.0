package com.tswmoodle2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.beans.Argomento;
import model.beans.Corso;
import model.beans.Lezione;
import model.dao.ArgomentoDaoImpl;
import model.dao.CorsoDaoImpl;
import model.dao.LezioneDaoImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;

@WebServlet("/last-resource")
public class LastResourceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lessonIdStr = request.getParameter("lesson");
        String topicIdStr = request.getParameter("topic");

        if (lessonIdStr == null || topicIdStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int lessonId = Integer.parseInt(lessonIdStr);
        int topicId = Integer.parseInt(topicIdStr);

        Lezione lesson = new LezioneDaoImpl().findById(lessonId);
        if (lesson == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Corso course = new CorsoDaoImpl().findByID(lesson.getIdCorso());
        if (course == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Argomento topic = new ArgomentoDaoImpl().findById(topicId);
        if (topic == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String filename = request.getParameter("filename");
        String urlFile = "file?file="+filename+"&id="+course.getIdCorso()+"&c=course";

        JSONArray coursesArray = new JSONArray();

        // Crea un oggetto per contenere i dati della risposta
        JSONObject courseObject = new JSONObject();
        courseObject.put("textResourceUrl", urlFile);
        courseObject.put("textResourceName", "\uD83D\uDCC4" + filename);
        courseObject.put("lessonCourse", "(" + course.getNome() + ") " + lesson.getTitolo());
        courseObject.put("teacherName", course.getCreatore().getCognome() + " " +course.getCreatore().getNome());
        courseObject.put("teacherPhotoUrl", "file?file="+course.getCreatore().getImg()+"&id="+course.getCreatore().getIdUtente()+"&c=user");
        coursesArray.add(courseObject);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // Aggiungi questo per consentire le richieste CORS
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        response.getWriter().write(coursesArray.toJSONString());
    }
}
