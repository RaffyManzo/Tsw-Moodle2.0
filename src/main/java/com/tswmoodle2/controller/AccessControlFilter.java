package com.tswmoodle2.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter(filterName = "AccessControlFilter", urlPatterns = "/new")
public class AccessControlFilter extends HttpFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inizializzazione del filtro (se necessario)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // Recuperiamo l'attributo "isAdmin" dalla sessione, senza creare una nuova sessione
        HttpSession session = httpServletRequest.getSession(false);


        Boolean isAdmin = (session != null) ? (Boolean) session.getAttribute("isAdmin") : null;

        if(session == null)
            System.out.println("Session not found");
        else
            System.out.println("Session found");
        if(isAdmin == null)
            System.out.println("isAdmin attribute not found");
        else
            System.out.println("isAdmin attribute found");

        // Recuperiamo il percorso della richiesta
        String path = httpServletRequest.getServletPath();
        System.out.println("Request path: " + path);  // Stampa il percorso della richiesta per il debugging

        // Verifica se il percorso è una risorsa pubblica
        boolean isPublicResource = path.startsWith("/WEB-INF/results/public") || !path.startsWith("/WEB-INF/results/private");
        boolean isALoginRequest = path.endsWith("login.html") || path.endsWith("registrazione.html");
        boolean isAdminResource = path.contains("/admin");


        // Se l'utente è autenticato e sta cercando di accedere alla pagina di login o registrazione, reindirizzalo alla home
        if (isAdmin != null && isALoginRequest) {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/home");

            return;
        }

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



        chain.doFilter(httpServletRequest, httpServletResponse);

    }

    @Override
    public void destroy() {
        // Pulizia del filtro (se necessario)
    }
}
