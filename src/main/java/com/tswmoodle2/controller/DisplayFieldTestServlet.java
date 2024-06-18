package com.tswmoodle2.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "DisplayFieldTestServlet", value = "/print")
public class DisplayFieldTestServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String address = request.getParameter("address");
        String nation = request.getParameter("nation");
        String birthDate = request.getParameter("birth-date");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String countryCode = request.getParameter("countryCode");
        String phoneNumber = request.getParameter("phoneNumber");
        String accountType = request.getParameter("account-type");

        // Impostare gli attributi della richiesta
        request.setAttribute("name", name);
        request.setAttribute("surname", surname);
        request.setAttribute("address", address);
        request.setAttribute("nation", nation);
        request.setAttribute("birthDate", birthDate);
        request.setAttribute("username", username);
        request.setAttribute("email", email);
        request.setAttribute("countryCode", countryCode);
        request.setAttribute("phoneNumber", phoneNumber);
        request.setAttribute("accountType", accountType);

        // Inoltrare la richiesta al file JSP
        request.getRequestDispatcher("/WEB-INF/results/private/test.jsp").forward(request, response);
    }
}
