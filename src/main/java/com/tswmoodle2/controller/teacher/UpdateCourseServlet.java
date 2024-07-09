package com.tswmoodle2.controller.teacher;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.beans.Corso;
import model.dao.CorsoDaoImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/update-course")
@MultipartConfig
public class UpdateCourseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(UpdateCourseServlet.class.getName());

    private static final String ERROR_VIEW = "/WEB-INF/results/public/error.jsp";
    private static final String COURSE_VIEW = "/WEB-INF/results/private/teacher/course.jsp";

    private static final String SEPARATOR = "/";
    private String UPLOAD_FOLDER;

    @Override
    public void init() throws ServletException {
        UPLOAD_FOLDER = getServletContext().getInitParameter("upload-path");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        if(checkField(request)) {
            Corso corso = new CorsoDaoImpl().findByID(
                    Integer.parseInt(request.getParameter("id")));

            if(corso == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "course attribute is null");
                return;
            }


            String title = request.getParameter("titolo");
            String description = request.getParameter("descrizione");
            Part imgPart = request.getPart("immagine");
            String img = imgPart != null && imgPart.getSize() > 0 ? getFileName(imgPart) : corso.getImmagine();
            double price = Double.parseDouble(request.getParameter("prezzo"));


            Corso updated = new Corso(corso.getIdCorso(), corso.getNomeCategoria(),
                    title, description, img, corso.getCertificazione(), corso.getDataCreazione(),
                    corso.getCreatore(), price, corso.getNumeroAcquisti(), corso.isDeleted());

            LOGGER.log(Level.INFO, updated.toString());
            LOGGER.log(Level.INFO, Boolean.toString(corso.equals(updated)));

            if(!corso.equals(updated)) {

                if (imgPart != null && imgPart.getSize() > 0) {
                    uploadImage(imgPart, corso.getIdCorso(), request, response);
                }

                LOGGER.log(Level.INFO, "Updating the course");
                new CorsoDaoImpl().update(updated);
                request.setAttribute("course", updated);
            }

            response.sendRedirect("course?courseID=" + corso.getIdCorso());

        } else {
            forwardWithError(request, response, List.of("Alcuni campi sono nulli"));
        }


    }

    private void uploadImage(Part fileParam, int courseId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = getFileName(fileParam);
        if (fileName == null || fileName.isEmpty()) {
            LOGGER.log(Level.SEVERE, "File name could not be determined.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File name could not be determined.");
            return;
        }

        String uploadPath = UPLOAD_FOLDER + SEPARATOR + "user" + SEPARATOR + courseId;
        LOGGER.log(Level.INFO, "Path for upload: {0}", uploadPath);

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            try {
                Path createdPath = Files.createDirectories(uploadDir.toPath());
                LOGGER.log(Level.INFO, "Directory created or already exists at: {0}", createdPath.toString());
                if (!Files.exists(createdPath)) {
                    LOGGER.log(Level.SEVERE, "Directory creation failed: {0}", createdPath.toString());
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create upload directory.");
                    return;
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to create directory: " + e.getMessage(), e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create upload directory.");
                return;
            }
        }

        String filePath = uploadPath + SEPARATOR + fileName;
        LOGGER.log(Level.INFO, "File path: {0}", filePath);

        File existingFile = new File(filePath);
        if (existingFile.exists()) {
            existingFile.delete();
        }

        try (InputStream input = fileParam.getInputStream()) {
            Files.copy(input, Path.of(filePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to save uploaded file: " + ex.getMessage(), ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save uploaded file.");
        }
    }

    private boolean checkField(HttpServletRequest request) throws ServletException, IOException {
        return request.getParameter("titolo") != null &&
                request.getParameter("prezzo") != null &&
                request.getParameter("id") != null;
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, List<String> errors) throws ServletException, IOException {
        request.setAttribute("errors", new ArrayList<>(errors));
        request.getRequestDispatcher(ERROR_VIEW).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}