package com.tswmoodle2.controller.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import jakarta.servlet.http.HttpSession;
import model.beans.Utenza;

import java.io.IOException;

public class CustomHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public CustomHttpServletResponseWrapper(HttpServletRequest request, HttpServletResponse response) {
        super( (HttpServletResponse) response);
        this.response = response;
        this.request = request;
    }

    @Override
    public void sendRedirect(String location) throws IOException {
            HttpSession session = request.getSession(false);
            Utenza user = (session != null) ? (Utenza) session.getAttribute("user") : null;
            boolean isALoginRequest = location.endsWith("login.html") || location.endsWith("registrazione.html");

            if (user != null && isALoginRequest) {
                location = (request.getContextPath() + "/home");
            }

        super.sendRedirect(location);
    }
}
