package com.tswmoodle2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Corso;
import model.beans.Lezione;
import model.dao.ArgomentoDaoImpl;
import model.dao.CategoriaDaoImpl;
import model.dao.CorsoDaoImpl;
import model.dao.LezioneDaoImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CourseServlet", value = "/course")
public class CourseServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("courseID");

        if(param != null ) {

            Corso corso =  new CorsoDaoImpl().findByID(Integer.parseInt(param));

            if(corso == null) {
                errorOccurs(request, response);
            } else {

                ArrayList<Lezione> lezioni = new LezioneDaoImpl().findAllByCorsoId(corso.getIdCorso());

                if (!lezioni.isEmpty() ) {

                    for (Lezione l : lezioni) {
                        l.setArgomenti(new ArgomentoDaoImpl().findAllByLezioneId(l.getId()));
                    }
                    request.setAttribute("lezioni", lezioni);
                }
            }

            request.setAttribute("categories", new CategoriaDaoImpl().getAllCategorie());



            request.setAttribute("course", corso);

            request.getRequestDispatcher("/WEB-INF/results/public/course.jsp").forward(request, response);
        } else {
            errorOccurs(request, response);
        }
    }

    public void errorOccurs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        errors.add("Errore generico");
        request.setAttribute("errors", errors);
        request.getRequestDispatcher("/WEB-INF/results/public/error.jsp").forward(request, response);
    }
}
