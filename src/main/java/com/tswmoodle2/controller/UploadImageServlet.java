package com.tswmoodle2.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
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

@WebServlet(name = "UploadImageServlet", urlPatterns = "/uploadImage")
@MultipartConfig
public class UploadImageServlet extends HttpServlet {

    private static final String SEPARATOR = "/";
    private String UPLOAD_FOLDER;
    private static final Logger LOGGER = Logger.getLogger(UploadImageServlet.class.getName());

    @Override
    public void init() {
        UPLOAD_FOLDER = getServletContext().getInitParameter("upload-path");
        LOGGER.log(Level.INFO, "Upload folder set to: " + UPLOAD_FOLDER);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Utenza user = (Utenza) request.getSession().getAttribute("user");
        if (user == null) {
            LOGGER.log(Level.SEVERE, "User session not found.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in.");
            return;
        }

        Part filePart = request.getPart("profilePicture");
        if (filePart == null) {
            LOGGER.log(Level.SEVERE, "No file uploaded or file part name mismatch.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No file uploaded.");
            return;
        }

        String fileName = getFileName(filePart);
        if (fileName == null || fileName.isEmpty()) {
            LOGGER.log(Level.SEVERE, "File name could not be determined.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File name could not be determined.");
            return;
        }

        String uploadPath = UPLOAD_FOLDER + SEPARATOR + "user" + SEPARATOR + user.getIdUtente();
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

        try (InputStream input = filePart.getInputStream()) {

            Files.copy(input, Path.of(filePath));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to save uploaded file: " + ex.getMessage(), ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save uploaded file.");
            return;
        }

        user.setImage(fileName);
        new UtenzaDaoImpl().update(user);
        request.getSession().setAttribute("user", user);

        response.sendRedirect("account");
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
