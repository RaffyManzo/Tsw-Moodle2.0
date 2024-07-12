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
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
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
                    LOGGER.log(Level.INFO, "upload course image");
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

        String uploadPath = UPLOAD_FOLDER + SEPARATOR + "course" + SEPARATOR + courseId;
        LOGGER.log(Level.INFO, "Path for upload: {0}", uploadPath);

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            try {
                Path createdPath = Files.createDirectories(uploadDir.toPath());
                LOGGER.log(Level.INFO, "Directory created or already exists at: {0}", createdPath.toString());
                // Verifica esistenza directory
                if (Files.exists(createdPath)) {
                    LOGGER.log(Level.INFO, "Confirmed: Directory exists at: {0}", createdPath.toString());
                } else {
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
            LOGGER.log(Level.INFO, "File already exists: {0}", filePath);
            // Option 1: Delete the existing file

        } else {
            String previusImg = new CorsoDaoImpl().findByID(courseId).getImmagine();
            String fileToDeletePath = uploadPath + SEPARATOR + previusImg;

            File deleteFile = new File(fileToDeletePath);
            LOGGER.log(Level.INFO, "Check if fileToDelete exists: {0}", deleteFile.exists());

            if(previusImg != null) {
                if (deleteFile.exists() && !previusImg.isEmpty()) {
                    deleteFileFromFolder(deleteFile);
                }
            }

            try (InputStream input = fileParam.getInputStream()) {

                Files.copy(input, Path.of(filePath));
                LOGGER.log(Level.INFO, "Image saved successfully");
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Failed to save uploaded file: " + ex.getMessage(), ex);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save uploaded file.");
                return;
            }
        }
    }

    private void deleteFileFromFolder(File deleteFile) {
        try {
            // Chiudere eventuali risorse ancora aperte
            closeFileResources(deleteFile);

            // Attendi prima di tentare di eliminare
            try {
                Thread.sleep(100); // Attendi 100 ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.log(Level.WARNING, "Thread interrupted", e);
            }

            // Tentativo di eliminazione del file
            if (deleteFile.delete()) {
                LOGGER.log(Level.INFO, "Previous file successfully eliminated");
            } else {
                LOGGER.log(Level.WARNING, "Failed to delete the previous file");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error deleting the previous file", e);
            throw new RuntimeException(e);
        }
    }

    private void closeFileResources(File file) throws IOException {
        System.gc();

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel channel = raf.getChannel();
             FileLock lock = channel.lock()) {
            // Forzare il rilascio del lock
            lock.release();
            LOGGER.log(Level.INFO, "File resources successfully closed for: {0}", file.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error closing file resources for: {0} {1}", new Object[]{file.getAbsolutePath(), e});
            throw e;
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