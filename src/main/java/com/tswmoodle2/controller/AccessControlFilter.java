package com.tswmoodle2.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter(filterName = "AccessControlFilter", urlPatterns = "/*")
public class AccessControlFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inizializzazione del filtro (se necessario)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Convertiamo la richiesta e la risposta in oggetti HTTP
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // Recuperiamo l'attributo "isAdmin" dalla sessione
        Boolean isAdmin = (Boolean) httpServletRequest.getSession().getAttribute("isAdmin");

        // Recuperiamo il percorso della richiesta
        String path = httpServletRequest.getServletPath();
        System.out.println(path);  // Stampa il percorso della richiesta (utile per il debugging)

        // Verifica se il percorso è una risorsa pubblica
        boolean isPublicResource = path.contains("/WEB-INF/results/public") || !path.startsWith("/WEB-INF/results/private");

        boolean isAdminResource = path.contains("/admin");

        // Se l'utente non è autenticato e sta tentando di accedere a una risorsa protetta
        if (isAdmin == null && !isPublicResource) {
            List<String> errors = new ArrayList<>();
            errors.add("Non hai i permessi per accedere a questa risorsa");
            httpServletRequest.setAttribute("errors", errors);
            httpServletRequest.getRequestDispatcher("/WEB-INF/results/public/error.jsp").forward(request, response);
            return; // Aggiungiamo return per assicurare che la catena di filtri venga bloccata
        }

        // Se l'utente non è un amministratore e sta tentando di accedere a una risorsa admin
        if (isAdmin != null && !isAdmin && isAdminResource) {
            List<String> errors = new ArrayList<>();
            errors.add("Non hai i permessi per accedere a questa risorsa");
            httpServletRequest.setAttribute("errors", errors);
            httpServletRequest.getRequestDispatcher("/WEB-INF/results/public/error.jsp").forward(request, response);
            return; // Aggiungiamo return per assicurare che la catena di filtri venga bloccata
        }


        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
        // Pulizia del filtro (se necessario)
    }
}
