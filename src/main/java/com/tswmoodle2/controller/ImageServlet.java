package com.tswmoodle2.controller;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet(name = "ImageServlet", urlPatterns = {"/getImage"})
public class ImageServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext sc = getServletContext();

        // Nella request troveremo il nome del file e l'id dell'utente, ogni file relativo a quell'utente si trova
        // in un'apposita cartella
        String fileName = request.getParameter("image");
        String userID = request.getParameter("id");

        // Leggo la risorsa utilizzando il servletContext

        /*
        * getResourceAsStream: Questo è un metodo di ServletContext.
        * Prende un percorso a un file come argomento e restituisce un InputStream per leggere il file.
        * Il file deve essere nel percorso del contesto dell’applicazione web
        * */

        InputStream file = sc.getResourceAsStream("WEB-INF/users-file/images/" + userID + "/" + fileName);
        OutputStream os = response.getOutputStream();

        // Se l'oggetto é nullo genero un messaggio di errore
        if (file == null) {

            response.setContentType("text/plain");
            os.write("Failed to send image".getBytes());
        }else {

            // Se non é nullo, invio l'immagine 1024 byte alla volta e lo invio tramite un OutputStream
            response.setContentType("image/jpeg");

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = file.read(buffer)) != -1) {

                os.write(buffer, 0, bytesRead);
            }
        }
    }
}