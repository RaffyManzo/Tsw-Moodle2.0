package com.tswmoodle2.controller.session;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/sessionController")
public class SessionController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);

        if (session != null) {
            if ("renew".equals(action)) {
                session.setMaxInactiveInterval(900); // 15 minuti TEST default=900, test=20
                response.getWriter().write("Session renewed");
            } else if ("logoff".equals(action)) {
                session.invalidate();
                response.getWriter().write("Session invalidated");
            }
        }
    }
}
