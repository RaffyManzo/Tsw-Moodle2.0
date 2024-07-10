package com.tswmoodle2.controller.teacher;

import jakarta.servlet.annotation.MultipartConfig;
import model.beans.Argomento;
import model.beans.Corso;
import model.beans.Lezione;
import model.dao.ArgomentoDaoImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CorsoDaoImpl;
import model.dao.LezioneDaoImpl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
@WebServlet("/lesson")
@MultipartConfig
public class LessonsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LessonsServlet.class.getName());

    private LezioneDaoImpl lezioneDao = new LezioneDaoImpl();
    private ArgomentoDaoImpl argomentoDao = new ArgomentoDaoImpl();

    private static final String LESSON_TEACHER_VIEW = "/WEB-INF/results/private/teacher/lessons.jsp";
    private static final String ERROR_VIEW = "/WEB-INF/results/public/error.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";

        if(request.getParameter("lezione") != null) {
            Lezione lezione = lezioneDao.findById(Integer.parseInt(request.getParameter("lezione")));
            lezione.setArgomenti(argomentoDao.findAllByLezioneId(lezione.getId()));
            request.setAttribute("lezione", lezione);
        }

        switch (action) {
            case "new":
            case "display":
                handleNewOrDisplay(request, response);
                break;
            default:
                errorOccurs(request, response);
                break;
        }
    }

    private void handleNewOrDisplay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseIdParam = request.getParameter("courseID");
        if (courseIdParam == null) {
            errorOccurs(request, response);
            return;
        }
        Corso corso = new CorsoDaoImpl().findByID(Integer.parseInt(courseIdParam));
        if (corso == null) {
            errorOccurs(request, response);
            return;
        } else {
            request.setAttribute("course", corso);
            request.getRequestDispatcher(LESSON_TEACHER_VIEW).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";

        LOGGER.log(Level.INFO, "Lessons action: {0}", action);

        switch (action) {
            case "createLesson":
                createLesson(request, response);
                break;
            case "createTopic":
                createTopic(request, response);
                break;
            case "deleteLesson":
                deleteLesson(request, response);
                break;
            case "deleteTopic":
                deleteTopic(request, response);
                break;
            default:
                errorOccurs(request, response);
                break;
        }
    }

    private void createLesson(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("lessonTitle");
        String description = request.getParameter("lessonDescription");
        int courseId = Integer.parseInt(request.getParameter("courseId"));

        String lessonID = request.getParameter("lessonID");
        Lezione lezione;

        if(lessonID != null) {
            lezione = lezioneDao.findById(Integer.parseInt(lessonID));
            if(lezione != null) {
                lezione.setTitolo(title);
                lezione.setDescrizione(description);
                lezioneDao.update(lezione);
            } else {
                errorOccurs(request, response);
                return;
            }
        } else {
            lezione = new Lezione();
            lezione.setTitolo(title);
            lezione.setDescrizione(description);
            lezione.setIdCorso(courseId);
            lezione.setId(lezioneDao.insertInto(lezione).getId());
        }

        response.sendRedirect("lesson?action=new&courseID=" + courseId + "&lezione=" + lezione.getId());
    }

    private void createTopic(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.INFO, "Creazione del topic");

        String title = request.getParameter("topicTitle");
        String description = request.getParameter("topicDescription");
        String lessonID = request.getParameter("lessonID");
        String topicId = request.getParameter("idargomento");
        String courseIdStr = request.getParameter("courseId");

        LOGGER.log(Level.INFO, "Parametri ricevuti: title = {0}, description = {1}, lessonID = {2}, topicId = {3}, courseIdStr = {4}",
                new Object[]{title, description, lessonID, topicId, courseIdStr});

        int lessonIdInt = Integer.parseInt(lessonID);
        int courseId = Integer.parseInt(courseIdStr);

        Argomento argomento;
        if (topicId != null && !topicId.isEmpty()) {
            argomento = argomentoDao.findById(Integer.parseInt(topicId));
            if (argomento != null) {
                argomento.setNome(title);
                argomento.setDescrizione(description);
                argomentoDao.update(argomento);
            } else {
                errorOccurs(request, response);
                return;
            }
        } else {
            argomento = new Argomento();
            argomento.setNome(title);
            argomento.setDescrizione(description);
            argomento.setIdLezione(lessonIdInt);
            argomento.setDataCaricamento(new Timestamp(System.currentTimeMillis()));
            LOGGER.log(Level.INFO, argomento.toString());
            argomentoDao.insertInto(argomento);
        }

        response.sendRedirect("lesson?action=new&courseID=" + courseId + "&lezione=" + lessonIdInt);
    }

    private void deleteTopic(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String topicId = request.getParameter("idargomento");

        if (topicId != null) {
            Argomento argomento = argomentoDao.findById(Integer.parseInt(topicId));
            if (argomento != null) {
                argomentoDao.delete(Integer.parseInt(topicId));
                response.sendRedirect("lesson?action=display&courseID=" +
                        request.getParameter("courseId") + "&lezione=" +
                        request.getParameter("lessonID"));
            } else {
                errorOccurs(request, response);
            }
        } else {
            errorOccurs(request, response);
        }
    }

    private void deleteLesson(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lessonID = request.getParameter("lessonID");

        if (lessonID != null) {
            Lezione lezione = lezioneDao.findById(Integer.parseInt(lessonID));
            if (lezione != null) {
                lezioneDao.delete(Integer.parseInt(lessonID));
                response.sendRedirect("lesson?action=new&courseID=" + lezione.getIdCorso());
            } else {
                errorOccurs(request, response);
            }
        } else {
            errorOccurs(request, response);
        }
    }


    private void errorOccurs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        errors.add("Errore generico");
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(ERROR_VIEW).forward(request, response);
    }
}
