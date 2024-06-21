package com.tswmoodle2.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.beans.Utenza;

import java.io.IOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter(filterName = "AccessControlFilter", urlPatterns = "/*")
public class AccessControlFilter extends HttpFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // Wrappiamo le richieste e le risposte
        CustomHttpServletRequestWrapper wrappedRequest = new CustomHttpServletRequestWrapper(httpServletRequest);
        CustomHttpServletResponseWrapper wrappedResponse = new CustomHttpServletResponseWrapper(httpServletRequest, httpServletResponse);

        // Recuperiamo il percorso della richiesta
        String path = httpServletRequest.getServletPath();

        // Verifica se il percorso è una risorsa pubblica
        boolean isPublicResource = path.startsWith("/WEB-INF/results/public") || !path.startsWith("/WEB-INF/results/private");
        boolean isALoginRequest = path.endsWith("login.html") || path.endsWith("registrazione.html");
        boolean isCoursesRequest = path.endsWith("/courses");

        // Recuperiamo l'attributo "isAdmin" dalla sessione, senza creare una nuova sessione
        HttpSession session = httpServletRequest.getSession(false);
        Utenza user = (session != null) ? (Utenza) session.getAttribute("user") : null;

        if (user == null && !isPublicResource && !isALoginRequest && !isCoursesRequest) {
            List<String> errors = new ArrayList<>();
            errors.add("Non hai i permessi per accedere a questa risorsa");
            httpServletRequest.setAttribute("errors", errors);
            wrappedResponse.sendRedirect(httpServletRequest.getContextPath() + "/WEB-INF/results/public/error.jsp");
            return; // Stop further processing
        }

        // Proceed with the request
        chain.doFilter(wrappedRequest, wrappedResponse);

        /*
        // Se l'utente è autenticato e sta cercando di accedere alla pagina di login o registrazione, reindirizzalo alla home
        if (isAdmin != null && isALoginRequest) {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/home");
            return; // Aggiungiamo return per assicurare che la catena di filtri venga bloccata
        }

        // Se l'utente non è autenticato e sta tentando di accedere a una risorsa protetta
        if (isAdmin == null && !isPublicResource) {
            List<String> errors = new ArrayList<>();
            errors.add("Non hai i permessi per accedere a questa risorsa");
            httpServletRequest.setAttribute("errors", errors);
            httpServletRequest.getRequestDispatcher("/WEB-INF/results/public/error.jsp").forward(request, response);
            return;
        }

        // Se l'utente non è un amministratore e sta tentando di accedere a una risorsa admin
        if (isAdmin != null && !isAdmin && isAdminResource) {
            List<String> errors = new ArrayList<>();
            errors.add("Non hai i permessi per accedere a questa risorsa");
            httpServletRequest.setAttribute("errors", errors);
            httpServletRequest.getRequestDispatcher("/WEB-INF/results/public/error.jsp").forward(request, response);
            return;
        } */
    }
}
