package com.tswmoodle2.controller.filter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;
import model.beans.Utenza;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private static final Logger LOGGER = Logger.getLogger(CustomHttpServletResponseWrapper.class.getName());

    public CustomHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        LOGGER.log(Level.INFO, "Path contains /WEB-INF/results/private: : {0}", path.contains("/WEB-INF/results/private"));
        HttpSession session = getSession(false);
        Utenza user = (session != null) ? (Utenza) session.getAttribute("user") : null;

        LOGGER.log(Level.INFO, "Session found: ", user);

        // Check for private resources
        if (path.contains("/WEB-INF/results/private")) {
            if (user == null) {
                List<String> errors = new ArrayList<>();
                errors.add("Non hai i permessi per accedere a questa risorsa, prova ad effettuare l'accesso");
                getRequest().setAttribute("errors", errors);
                path = "/WEB-INF/results/public/error.jsp";
            } else {
                // Check for teacher-specific resources
                if (path.contains("teacher") && !user.getTipo().equals("D")) {
                    List<String> errors = new ArrayList<>();
                    errors.add("Non hai i permessi per accedere a questa risorsa, prova ad effettuare l'accesso come docente.");
                    getRequest().setAttribute("errors", errors);
                    path = "/WEB-INF/results/public/error.jsp";
                } else {
                    return super.getRequestDispatcher(path);
                }
            }
        }

        // Check for admin resources
        boolean isAdminResource = path.contains("/WEB-INF/results/admin");
        if (isAdminResource) {
            if(session != null) {
                if (session.getAttribute("isAdmin").equals(Boolean.TRUE)) {
                    return super.getRequestDispatcher(path);
                } else {
                        List<String> errors = new ArrayList<>();
                        errors.add("Non hai i permessi per accedere a questa risorsa, prova ad effettuare l'accesso");
                        getRequest().setAttribute("errors", errors);
                        path = "/WEB-INF/results/public/error.jsp";
                }
            } else {
                List<String> errors = new ArrayList<>();
                errors.add("Non hai i permessi per accedere a questa risorsa, prova ad effettuare l'accesso");
                getRequest().setAttribute("errors", errors);
                path = "/WEB-INF/results/public/error.jsp";
            }
        }

        return super.getRequestDispatcher(path);
    }
}