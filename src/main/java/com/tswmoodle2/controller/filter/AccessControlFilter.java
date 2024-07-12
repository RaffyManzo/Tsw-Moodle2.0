package com.tswmoodle2.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import model.beans.Utenza;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter(filterName = "AccessControlFilter", urlPatterns = "/*")
public class
AccessControlFilter extends HttpFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // Wrappiamo le richieste e le risposte
        CustomHttpServletRequestWrapper wrappedRequest = new CustomHttpServletRequestWrapper(httpServletRequest);
        CustomHttpServletResponseWrapper wrappedResponse = new CustomHttpServletResponseWrapper(httpServletRequest, httpServletResponse);

        manageSessionExpire(httpServletRequest, httpServletResponse);


        // Proceed with the request
        chain.doFilter(wrappedRequest, wrappedResponse);
    }

    private void manageSessionExpire(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            Long lastAccessedTime = (Long) session.getAttribute("lastAccessedTime");
            long currentTime = System.currentTimeMillis();

            if (lastAccessedTime != null) {
                long timeSinceLastAccess = currentTime - lastAccessedTime;
                long remainingTime = session.getMaxInactiveInterval() * 1000L - timeSinceLastAccess;

                // Avvisa l'utente se la sessione sta per scadere (ad esempio, entro 1 minuto)
                if (remainingTime < 60000) { // 1 minuto
                    // Esegui l'azione per avvisare l'utente, ad esempio impostando un attributo di richiesta
                    request.setAttribute("sessionExpiringSoon", true);
                }
            }

            // Aggiorna l'ultimo tempo di accesso
            session.setAttribute("lastAccessedTime", currentTime);
        }
    }

    private boolean control(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
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
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/WEB-INF/results/public/error.jsp");
            return true; // Stop further processing
        }

        return false;
    }


}
