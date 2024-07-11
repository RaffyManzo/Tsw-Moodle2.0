package com.tswmoodle2.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Corso;
import model.beans.Utenza;
import model.dao.CorsoDaoImpl;
import model.dao.UtenzaDaoImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter(filterName = "FileFilter", urlPatterns = {"/file"})
public class FileFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(FileFilter.class.getName());
    private static final String USER_FILE = "user";
    private static final String COURSE_FILE = "course";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.log(Level.INFO, "FileFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Prelevo i parametri
        String id = httpRequest.getParameter("id");
        String fileName = httpRequest.getParameter("file");
        String contest = httpRequest.getParameter("c");

        LOGGER.log(Level.INFO, "Request received with parameters: id={0}, file={1}, contest={2}", new Object[]{id, fileName, contest});

        if(fileName.equals("default.png")) {
            chain.doFilter(request, response);
            return;
        }
        try {
            switch (contest) {
                case USER_FILE:
                    if (!checkUserRequest(httpRequest, id)) {
                        httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized user request");
                        LOGGER.log(Level.WARNING, "Unauthorized user request for id={0}", id);
                        return;
                    }
                    break;
                case COURSE_FILE:
                    if (!checkCourseRequest(httpRequest, id, fileName)) {
                        httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized course request");
                        LOGGER.log(Level.WARNING, "Unauthorized course request for id={0} and file={1}", new Object[]{id, fileName});
                        return;
                    }
                    break;
                default:
                    chain.doFilter(request, response);
                    return;
            }

            // Proceed with the request
            chain.doFilter(request, response);
            LOGGER.log(Level.INFO, "Request processed successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing request", e);
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private boolean checkUserRequest(HttpServletRequest request, String userID) {
        Utenza user = (Utenza) request.getSession(false).getAttribute("user");

        if(new UtenzaDaoImpl().findByID(Integer.parseInt(userID)).getTipo().equals("D"))
            return true;

        // Se l'utente non é loggato o se il campo user é nullo o sta facendo una richiesta non autorizzata viene ritornato false
        if (user == null) {
            LOGGER.log(Level.WARNING, "User not logged in or userID is null: user={0}, userID={1}", new Object[]{user, userID});
            return false;
        }

        if(user.getTipo().equals("D"))
            return true;

        boolean isAuthorized = user.getIdUtente() == Integer.parseInt(userID);
        if (!isAuthorized) {
            LOGGER.log(Level.WARNING, "User ID mismatch: loggedUser={0}, requestUserID={1}", new Object[]{user.getIdUtente(), userID});
        }
        return isAuthorized;
    }

    private boolean checkCourseRequest(HttpServletRequest request, String courseId, String filename) {
        Utenza user = (Utenza) request.getSession(false).getAttribute("user");
        if (courseId == null) {
            LOGGER.log(Level.WARNING, "Course ID is null");
            return false;
        }

        Corso course = new CorsoDaoImpl().findByID(Integer.parseInt(courseId));
        if (course == null) {
            LOGGER.log(Level.WARNING, "Course not found for ID: {0}", courseId);
            return false;
        }

        if (filename.equals(course.getImmagine())) {
            return true;
        } else {
            if (user == null) {
                LOGGER.log(Level.WARNING, "User not logged in");
                return false;
            } else {
                boolean isPurchased = isPurchased(course, user);
                if (!isPurchased) {
                    LOGGER.log(Level.WARNING, "User has not purchased the course: userID={0}, courseID={1}", new Object[]{user.getIdUtente(), courseId});
                }
                return isPurchased;
            }
        }
    }

    private boolean isPurchased(Corso corso, Utenza user) {
        ArrayList<Corso> corsiAcquistati = new CorsoDaoImpl().getCorsiAcquistati(user.getIdUtente());

        for (Corso c : corsiAcquistati) {
            if (c.getIdCorso() == corso.getIdCorso()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void destroy() {
        LOGGER.log(Level.INFO, "FileFilter destroyed");
    }
}
