package com.tswmoodle2.controller;


import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.file.Files;


@WebServlet(name = "FileServlet", urlPatterns = {"/file"})
public class FileServlet extends HttpServlet {

    private String UPLOAD_FOLDER;

    public void init() {
        UPLOAD_FOLDER = getServletContext().getInitParameter("upload-path");;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletContext sc = getServletContext();

        // Nella request troveremo il nome del file e l'id dell'utente, ogni file relativo a quell'utente si trova
        // in un'apposita cartella
        String fileName = request.getParameter("file");
        String userID = request.getParameter("id");

        String contest = request.getParameter("c");

        // Leggo la risorsa utilizzando il servletContext

        /*
         * getResourceAsStream: Questo è un metodo di ServletContext.
         * Prende un percorso a un file come argomento e restituisce un InputStream per leggere il file.
         * Il file deve essere nel percorso del contesto dell’applicazione web
         * */
        File f = userID == null ?
                    new File(UPLOAD_FOLDER + "/" + contest + "/" + fileName) :
                    new File(UPLOAD_FOLDER + "/" + contest + "/" + userID + "/" + fileName);
        String contentType = Files.probeContentType(f.toPath());


        
        InputStream file = f.toURI().toURL().openStream();
        OutputStream os = response.getOutputStream();

        response.setContentType(contentType);

        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = file.read(buffer)) != -1) {

            os.write(buffer, 0, bytesRead);
        }

    }

    public static String getContentType(InputStream inputStream) throws IOException {
        // Crea un URLConnection con il tuo InputStream
        String contentType = URLConnection.guessContentTypeFromStream(inputStream);
        return contentType != null ? contentType : "Unknown";
    }
}