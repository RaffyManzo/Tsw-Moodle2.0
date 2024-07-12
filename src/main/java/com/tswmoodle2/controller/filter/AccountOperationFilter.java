package com.tswmoodle2.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter(filterName = "AccessControlFilter", urlPatterns = "/account")
public class AccountOperationFilter extends HttpFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // Wrappiamo le richieste e le risposte
        CustomHttpServletRequestWrapper wrappedRequest = new CustomHttpServletRequestWrapper(httpServletRequest);
        CustomHttpServletResponseWrapper wrappedResponse = new CustomHttpServletResponseWrapper(httpServletRequest, httpServletResponse);

        HttpSession session = httpServletRequest.getSession(false);

        if(session == null || session.getAttribute("user") != null) {
            List<String> errors = new ArrayList<>();
            errors.add("Non hai i permessi per accedere a questa risorsa");
            httpServletRequest.setAttribute("errors", errors);
            httpServletRequest.getRequestDispatcher("/WEB-INF/results/public/error.jsp").forward(request, response);
            return; // Stop further processing
        }

        chain.doFilter(wrappedRequest, wrappedResponse);

    }
}
