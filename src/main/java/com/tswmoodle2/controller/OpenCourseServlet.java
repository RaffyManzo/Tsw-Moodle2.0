package com.tswmoodle2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Corso;
import model.beans.Lezione;
import model.beans.Utenza;
import model.dao.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "OpenCourseServlet", value = "/openCourse")
public class OpenCourseServlet extends HttpServlet {

    private static final String STUDENT = "S";
    private static final String COURSE_PRIVATE_VIEW = "/WEB-INF/results/private/course.jsp";

    private static final String ERROR_VIEW = "/WEB-INF/results/public/error.jsp";
    private static final Logger LOGGER = Logger.getLogger(OpenCourseServlet.class.getName());


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String param = request.getParameter("courseID");
        Utenza user = (Utenza) request.getSession().getAttribute("user");

        if (param == null) {
            errorOccurs(request, response);
            return;
        }

        LOGGER.log(Level.INFO, "User is student: {0}", user.getTipo().equals(STUDENT));


        if(user.getTipo().equals(STUDENT)) {
            serveGeneric(request, response, param, user);
        }
        else {
            errorOccurs(request, response);
        }

    }

    private void setLezioniAttribute(HttpServletRequest request, Corso corso) {
        ArrayList<Lezione> lezioni = new LezioneDaoImpl().findAllByCorsoId(corso.getIdCorso());

        if (!lezioni.isEmpty()) {

            for (Lezione l : lezioni) {
                l.setArgomenti(new ArgomentoDaoImpl().findAllByLezioneId(l.getId()));
            }
            request.setAttribute("lezioni", lezioni);
        }
    }

    private void serveGeneric(HttpServletRequest request, HttpServletResponse response, String param, Utenza user) throws ServletException, IOException {
        Corso corso = new CorsoDaoImpl().findByID(Integer.parseInt(param));

        if (corso == null) {
            LOGGER.log(Level.INFO, "Course null");

            errorOccurs(request, response);
        } else {

            if(isPurchased(corso, user)) {

                LOGGER.log(Level.INFO, "Course purchased");
                setLezioniAttribute(request, corso);

                request.setAttribute("categories", new CategoriaDaoImpl().getAllCategorie());
                request.setAttribute("course", corso);
                request.setAttribute("cartID", new CartDaoImpl().getCartIDByUser(user.getIdUtente()));


                request.getRequestDispatcher(COURSE_PRIVATE_VIEW).forward(request, response);
            }
            else {
                LOGGER.log(Level.INFO, "Course not purchased");

                errorOccurs(request, response);
            }
        }


    }

    private void errorOccurs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        errors.add("Errore generico");
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(ERROR_VIEW).forward(request, response);

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
}
