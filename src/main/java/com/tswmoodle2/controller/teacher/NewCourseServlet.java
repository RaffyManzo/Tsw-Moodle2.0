package com.tswmoodle2.controller.teacher;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.beans.Corso;
import model.beans.Utenza;
import model.dao.CategoriaDaoImpl;
import model.dao.CorsoDaoImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/new-course")
@MultipartConfig
public class NewCourseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(NewCourseServlet.class.getName());

    private static final String ERROR_VIEW = "/WEB-INF/results/public/error.jsp";
    private static final String NEW_COURSE_VIEW = "/WEB-INF/results/private/teacher/createCourse.jsp";
    private static final String HOME_VIEW = "/WEB-INF/results/private/teacher/home.jsp";

    private static final String SEPARATOR = "/";
    private String UPLOAD_FOLDER;

    @Override
    public void init() throws ServletException {
        UPLOAD_FOLDER = getServletContext().getInitParameter("upload-path");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        serveNewCourseRequest(request, response);
    }

    private void serveNewCourseRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utenza user = ((Utenza)request.getSession().getAttribute("user"));
        if(user == null) {
            forwardWithError(request, response, List.of("Azione non consentita, effettua l'accesso"));
            return;
        }
        if(!user.getTipo().equals("D")) {
            forwardWithError(request, response, List.of("Azione non consentita per il tuo tipo di account"));
            return;
        }

        String action = request.getParameter("action");

        if (action == null) {
            action = "new";
        }

        switch (action) {
            case "redirect":
                request.setAttribute("categories", new CategoriaDaoImpl().getAllCategorie());
                request.getRequestDispatcher(NEW_COURSE_VIEW).forward(request, response);
                break;
            case "new":
                createNewCourse(request, response);
                break;
            case "search":
                request.setAttribute("categories", new CategoriaDaoImpl().getAllCategorie());
                String searchParam = request.getParameter("search-param");
                if(searchParam == null) {
                    searchParam = " ";
                }
                request.setAttribute("courses", new CorsoDaoImpl().searchCreatorCoursesByName(searchParam, user.getIdUtente()));
                request.getRequestDispatcher(HOME_VIEW).forward(request, response);
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
        String image = getFileName(request.getPart("immagine")); // Gestisci il caricamento dell'immagine
        String category = request.getParameter("categoria");
        String priceStr = request.getParameter("prezzo");
        Utenza user = (Utenza) request.getSession().getAttribute("user");

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

        Corso newCorso = new Corso(0, category, title, description, image, null,
                getToday(), user, price, 0, Boolean.TRUE);

        uploadCourseImage(request, response, new CorsoDaoImpl().insertInto(newCorso).getIdCorso());

        // Dopo la creazione, puoi reindirizzare a una pagina di successo o alla lista dei corsi
        response.sendRedirect("home"); // Adatta questo URL alla tua applicazione
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        serveNewCourseRequest(request, response);
    }


    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, List<String> errors) throws ServletException, IOException {

        request.setAttribute("errors", new ArrayList<>(errors));
        request.getRequestDispatcher(ERROR_VIEW).forward(request, response);
    }

    private void uploadCourseImage(HttpServletRequest request, HttpServletResponse response, int idcorso) throws ServletException, IOException {

        Part filePart = request.getPart("immagine");

        String fileName = getFileName(filePart);
        if (fileName == null || fileName.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File name could not be determined.");
            return;
        }

        String uploadPath = UPLOAD_FOLDER + SEPARATOR + "course" + SEPARATOR + idcorso;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            try {
                Path createdPath = Files.createDirectories(uploadDir.toPath());
                // Verifica esistenza directory
                if (!Files.exists(createdPath)) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create upload directory.");
                    return;
                }
            } catch (IOException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create upload directory.");
                return;
            }
        }

        String filePath = uploadPath + SEPARATOR + fileName;

        // Check if the file already exists
        File existingFile = new File(filePath);
        if (existingFile.exists()) {
            // Option 1: Delete the existing file

        } else {

            try (InputStream input = filePart.getInputStream()) {

                Files.copy(input, Path.of(filePath));
            } catch (IOException ex) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save uploaded file.");
                return;
            }
        }
    }


    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private Date getToday() {

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String todayStr = sdf.format(today.getTime());
        Date todayObj = null;

        // Converte le stringhe di data in oggetti Date
        try {
            todayObj = convertStringToDate(todayStr, "dd/MM/yyyy");
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }

        return todayObj;
    }

    public static Date convertStringToDate(String dateStr, String formatStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.parse(dateStr);
    }
}