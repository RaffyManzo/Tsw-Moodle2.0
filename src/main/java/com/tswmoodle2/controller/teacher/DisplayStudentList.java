package com.tswmoodle2.controller.teacher;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import model.beans.Argomento;
import model.beans.Corso;
import model.beans.Lezione;
import model.beans.Utenza;
import model.dao.ArgomentoDaoImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CorsoDaoImpl;
import model.dao.LezioneDaoImpl;
import model.dao.UtenzaDaoImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/students")
@MultipartConfig
public class DisplayStudentList extends HttpServlet {

    private static final String ERROR_VIEW = "/WEB-INF/results/public/error.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(request.getParameter("courseID") != null) {

            List<Utenza> users = new CorsoDaoImpl().getUtentiAcquistati(Integer.parseInt(
                    request.getParameter("courseID")
            ));


            request.setAttribute("users",
                    new ArrayList<>(users.stream().filter(utenza -> utenza.getIdUtente() != 0).collect(Collectors.toList())));
            request.getRequestDispatcher("/WEB-INF/results/private/teacher/students.jsp").forward(request, response);
        }else {
            List<String> errors = new ArrayList<>();
            errors.add("Errore generico");
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(ERROR_VIEW).forward(request, response);
        }
    }
}