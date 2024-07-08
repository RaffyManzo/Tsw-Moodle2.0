package com.tswmoodle2.controller.teacher;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.dao.CategoriaDaoImpl;

import java.io.IOException;
import java.util.List;

@WebServlet("/new-course")
public class NewCourseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String ERROR_VIEW = "/WEB-INF/results/public/error.jsp";
    private static final String NEW_COURSE_VIEW = "/WEB-INF/results/private/teacher/createCourse.jsp";



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "redirect":
                request.setAttribute("categories", new CategoriaDaoImpl().getAllCategorie());
                request.getRequestDispatcher(NEW_COURSE_VIEW).forward(request, response);
                break;
            case "new":
                createNewCourse(request, response);
                break;
            default:
                forwardWithError(request, response, List.of("Azione non consentita."));
                break;
        }
    }

    private void createNewCourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupera i parametri dal form di creazione del corso
        String title = request.getParameter("titolo");
        String description = request.getParameter("descrizione");
        String image = request.getParameter("immagine"); // Gestisci il caricamento dell'immagine
        String category = request.getParameter("categoria");
        String priceStr = request.getParameter("prezzo");

        // Valida i parametri
        if (title == null || description == null || image == null || category == null || priceStr == null) {
            forwardWithError(request, response, List.of("Alcuni campi sono nulli."));
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            forwardWithError(request, response, List.of("Prezzo non valido"));
            return;
        }

        // Esegui la logica per creare un nuovo corso (es. salva nel database)
        // Questa parte deve essere implementata secondo le tue specifiche esigenze
        // Ad esempio:
        // Course newCourse = new Course(title, description, image, category, price);
        // courseService.save(newCourse);

        // Dopo la creazione, puoi reindirizzare a una pagina di successo o alla lista dei corsi
        response.sendRedirect("courses-list.html"); // Adatta questo URL alla tua applicazione
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, List<String> errors) throws ServletException, IOException {
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(ERROR_VIEW).forward(request, response);
    }
}