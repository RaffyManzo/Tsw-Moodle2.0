package com.tswmoodle2.controller;

import java.io.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "check-cookies", value = "/check-cookies")
public class CheckCookies extends HttpServlet {
    private String message;

    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        RequestDispatcher dispatcher =
                request.getRequestDispatcher("/login.html");
        dispatcher.forward(request, response);
    }

    public void destroy() {
    }
}