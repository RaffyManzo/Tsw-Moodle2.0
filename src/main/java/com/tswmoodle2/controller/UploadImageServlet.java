package com.tswmoodle2.controller;


import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.beans.Utenza;
import model.dao.UtenzaDaoImpl;

@WebServlet(name = "UploadImageServlet" ,urlPatterns = "/uploadImage")
@MultipartConfig
public class UploadImageServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "user";
    private static final String SEPARATOR = "/";
    private String UPLOAD_FOLDER;
    private static final Logger LOGGER = Logger.getLogger(UploadImageServlet.class.getName());

    public void init() {
        UPLOAD_FOLDER = getServletContext().getInitParameter("upload-path");;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the user ID from session
        Utenza user = (Utenza) request.getSession().getAttribute("user"); // or get it from session

        // Get the uploaded file part
        Part filePart = request.getPart("profilePicture");
        String fileName = getFileName(filePart);

        // Create path components to save the file
        String uploadPath = UPLOAD_FOLDER + SEPARATOR +  UPLOAD_DIR + SEPARATOR + user.getIdUtente();

        LOGGER.log(Level.INFO, "Path che cerco e se non esiste creo: {0}", uploadPath);

        // Create the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Save the file
        String filePath = uploadPath + SEPARATOR + fileName;

        LOGGER.log(Level.INFO, "File che sto cercando: {0}", filePath);

        File file = new File(filePath);
        try (var input = filePart.getInputStream()) {
            File fileToDelete = new File(UPLOAD_FOLDER + SEPARATOR +  UPLOAD_DIR + SEPARATOR + user.getIdUtente() +SEPARATOR+user.getImg());
            if(fileToDelete.exists()){
                boolean deleteResult = fileToDelete.delete();
                if (!deleteResult) {
                    LOGGER.log(Level.WARNING, "Non è stato possibile cancellare il file: {0}", fileToDelete.getAbsolutePath());
                }
            } else {
                LOGGER.log(Level.WARNING, "Il file da cancellare non esiste: {0}", fileToDelete.getAbsolutePath());
            }
            Files.copy(input, file.toPath());
        } catch (FileAlreadyExistsException ex) {
            throw new RuntimeException(ex);
        }



        new UtenzaDaoImpl().updateImage(user.getIdUtente(), fileName);
        request.getSession().setAttribute("user", new UtenzaDaoImpl().findByID(user.getIdUtente()));

        // Redirect to success page or send a success response
        response.sendRedirect("account"); // or respond with a success message
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
