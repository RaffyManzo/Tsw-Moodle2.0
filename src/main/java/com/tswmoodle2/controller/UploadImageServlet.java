package com.tswmoodle2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.beans.Utenza;
import model.dao.UtenzaDaoImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "UploadImageServlet", urlPatterns = "/modifyPic")
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

        if (deletePic(request, response)) {
            response.sendRedirect("account");
        } else {

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
            String fileToDeletePath = uploadPath + SEPARATOR + user.getImg();
            LOGGER.log(Level.INFO, "File path: {0} - IFExists deleting {1}",
                    new Object[]{filePath, fileToDeletePath});

            // Check if the file already exists
            File existingFile = new File(filePath);
            if (existingFile.exists()) {
                LOGGER.log(Level.INFO, "File already exists: {0}", filePath);
                // Option 1: Delete the existing file

            } else {
                File deleteFile = new File(fileToDeletePath);
                LOGGER.log(Level.INFO, "Check if fileToDelete exists: {0}", deleteFile.exists());

                if(user.getImg() != null) {
                    if (deleteFile.exists() && !user.getImg().isEmpty()) {
                        deleteFileFromFolder(deleteFile);
                    }
                }

                try (InputStream input = filePart.getInputStream()) {

                    Files.copy(input, Path.of(filePath));
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, "Failed to save uploaded file: " + ex.getMessage(), ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save uploaded file.");
                    return;
                }
            }

            user.setImage(fileName);
            new UtenzaDaoImpl().update(user);
            request.getSession().setAttribute("user", user);

            response.sendRedirect("account");
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

    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private boolean deletePic(HttpServletRequest request, HttpServletResponse response) {
        String delete = request.getParameter("delete");
        if (delete.equals("TRUE")) {
            Utenza user = (Utenza) request.getSession().getAttribute("user");
            String uploadPath = UPLOAD_FOLDER + SEPARATOR + "user" + SEPARATOR + user.getIdUtente();
            String fileToDeletePath = uploadPath + SEPARATOR + user.getImg();


            File deleteFile = new File(fileToDeletePath);
            LOGGER.log(Level.INFO, "Check if fileToDelete exists: {0}", deleteFile.exists());

            if(user.getImg() != null) {
                if (deleteFile.exists() && !user.getImg().isEmpty()) {
                    deleteFileFromFolder(deleteFile);
                }
            }

            user.setImage("");
            new UtenzaDaoImpl().update(user);
            request.getSession().setAttribute("user", user);

            return true;
        } else
            return false;
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
}