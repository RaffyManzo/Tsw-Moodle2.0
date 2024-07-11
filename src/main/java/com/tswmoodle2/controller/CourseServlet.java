package com.tswmoodle2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Corso;
import model.beans.Lezione;
import model.beans.Utenza;
import model.dao.ArgomentoDaoImpl;
import model.dao.CategoriaDaoImpl;
import model.dao.CorsoDaoImpl;
import model.dao.LezioneDaoImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CourseServlet", value = "/course")
public class CourseServlet extends HttpServlet {

    private static final String TEACHER = "D";
    private static final String STUDENT = "S";
    private static final String COURSE_PUBLIC_VIEW = "/WEB-INF/results/public/course.jsp";
    private static final String COURSE_TEACHER_VIEW = "/WEB-INF/results/private/teacher/course.jsp";

    private static final String ERROR_VIEW = "/WEB-INF/results/public/error.jsp";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("courseID");
        Utenza user = (Utenza) request.getSession().getAttribute("user");

        if(param == null ) {
            errorOccurs(request, response);
            return;
        }


        if(user == null) {
            user = new Utenza(0, "S");
        }

        switch (user.getTipo()) {
            case TEACHER:
                serveTeacher(request, response, param, user);
                break;
            case STUDENT:
            default:
                serveGeneric(request, response, param);
                break;
        }

    }

    private void serveTeacher(HttpServletRequest request, HttpServletResponse response, String param, Utenza user) throws ServletException, IOException {
        Corso corso =  new CorsoDaoImpl().findByID(Integer.parseInt(param));

        if(corso == null) {
            errorOccurs(request, response);
        } else {
            if(corso.getCreatore().getIdUtente() != user.getIdUtente()) {
                errorOccurs(request, response);
            } else {
                setLezioniAttribute(request, corso);
            }


            request.setAttribute("course", corso);
            request.getRequestDispatcher(COURSE_TEACHER_VIEW).forward(request, response);
        }
    }

    private void setLezioniAttribute(HttpServletRequest request, Corso corso) {
        ArrayList<Lezione> lezioni = new LezioneDaoImpl().findAllByCorsoId(corso.getIdCorso());

        if (!lezioni.isEmpty() ) {

            for (Lezione l : lezioni) {
                l.setArgomenti(new ArgomentoDaoImpl().findAllByLezioneId(l.getId()));
            }
            request.setAttribute("lezioni", lezioni);
        }
    }

    private boolean isPurchased(Corso corso, Utenza user) {
        ArrayList<Corso> corsiAcquistati = new CorsoDaoImpl().
                getCorsiAcquistati(user.getIdUtente());

        for (Corso c : corsiAcquistati) {
            if(c.getIdCorso() == corso.getIdCorso())
                return true;
        }

        return false;
    }

    private void serveGeneric(HttpServletRequest request, HttpServletResponse response, String param) throws ServletException, IOException {
        Corso corso =  new CorsoDaoImpl().findByID(Integer.parseInt(param));

        if(corso == null) {
            errorOccurs(request, response);
        } else {
            Utenza user = (Utenza) request.getSession().getAttribute("user");

            if(user != null){
                request.setAttribute("isPurchased",
                        isPurchased(corso, user));
            }
            setLezioniAttribute(request, corso);
        }

        request.setAttribute("categories", new CategoriaDaoImpl().getAllCategorie());



        request.setAttribute("course", corso);

        request.getRequestDispatcher(COURSE_PUBLIC_VIEW).forward(request, response);
    }

    private void errorOccurs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        errors.add("Errore generico");
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(ERROR_VIEW).forward(request, response);

    }
}
