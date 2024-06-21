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
        if (path.contains("/WEB-INF/results/private")) {
            HttpSession session = getSession(false);
            Utenza user = (session != null) ? (Utenza) session.getAttribute("user") : null;

            LOGGER.log(Level.INFO, "Sessione trovata: ", user);
            LOGGER.log(Level.INFO, "Path before check : {0}", path);


            if (user == null) {
                List<String> errors = new ArrayList<>();
                errors.add("Non hai i permessi per accedere a questa risorsa");
                getRequest().setAttribute("errors", errors);
                path = "/WEB-INF/results/public/error.jsp";
            } else {
                LOGGER.log(Level.INFO, "Path before check : {0}", path);

                return super.getRequestDispatcher(path);
            }
        }
        return super.getRequestDispatcher(path);
    }
}