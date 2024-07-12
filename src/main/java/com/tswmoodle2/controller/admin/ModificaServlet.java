package com.tswmoodle2.controller.admin;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.beans.Categoria;
import model.beans.Corso;
import model.beans.Utenza;
import model.dao.CategoriaDaoImpl;
import model.dao.CorsoDaoImpl;
import model.dao.UtenzaDaoImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet(name = "ModificaServlet", value = "/ModificaServlet")
public class ModificaServlet extends HttpServlet {
    private static ArrayList<String> badInput = new ArrayList<>();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tipo = request.getParameter("tipo");
        String elemento=request.getParameter("id");
        String nome=request.getParameter("nome");
        request.setAttribute("tipo", tipo);
        request.setAttribute("id", elemento);
        System.out.println(nome);
        if(nome==null) {
            switch (tipo) {
                case "utenza":
                    UtenzaDaoImpl u = new UtenzaDaoImpl();
                    Utenza utenza= u.findByID(Integer.parseInt(elemento));
                    request.setAttribute("elemento", utenza);
                    break;
                case "corso":
                    CorsoDaoImpl c = new CorsoDaoImpl();
                    Corso corso= c.findByID(Integer.parseInt(elemento));
                    request.setAttribute("elemento", corso);
                    break;
            }
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/results/admin/modifica.jsp");
            rd.forward(request, response);
        }else {

            switch (tipo) {
                case "utenza":
                    UtenzaDaoImpl u = new UtenzaDaoImpl();
                    Utenza utenza=u.findByID(Integer.parseInt(elemento));
                    String accountType = request.getParameter("tipoUtente");
                    String surname = request.getParameter("cognome");
                    String address = request.getParameter("indirizzo");
                    String nation = request.getParameter("nazione");
                    String birthDate = request.getParameter("dataNascita");
                    String username = request.getParameter("username");
                    String email = request.getParameter("email");
                    String phoneNumber = request.getParameter("telefono");
                    if (!validateInputs(nome, surname, address, nation, birthDate, username, email, phoneNumber)) {
                        error(request, response, "Alcuni campi sono vuoti o inseriti in modo errato");
                        return;
                    }
                    if(usernameAlreadyUsed(username)) {
                        if(!username.equals(utenza.getUsername())) {
                            error(request, response, "Username giá in uso");
                            return;
                        }
                    }

                    if(emailAlreadyUsed(email)) {
                        if(!email.equals(utenza.getEmail())) {
                            error(request, response, "Email giá in uso");
                            return;
                        }
                    }

                    try {
                        utenza.setNome(nome);
                        utenza.setCognome(surname);
                        utenza.setTipo(accountType);
                        utenza.setNazione(nation);
                        utenza.setIndirizzo(address);
                        utenza.setDataNascita(parseDate(birthDate));
                        utenza.setUsername(username);
                        utenza.setEmail(email);
                        utenza.setTelefono(phoneNumber);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    u.update(utenza);
                    break;
                case "corso":
                    CorsoDaoImpl c = new CorsoDaoImpl();
                    UtenzaDaoImpl ut = new UtenzaDaoImpl();
                    Corso corso = null;
                    try {
                        corso = new Corso(Integer.parseInt(elemento), request.getParameter("categoria"), nome,
                                request.getParameter("descrizione"), request.getParameter("immagine"), request.getParameter("certificazione"),
                                parseDate(request.getParameter("creazione")), ut.findByID(Integer.parseInt(request.getParameter("creatore"))),
                                Double.parseDouble(request.getParameter("prezzo")), Integer.parseInt(request.getParameter("acquisti")),
                                Boolean.getBoolean(request.getParameter("isDeleted")));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    c.update(corso);
                    break;

                case "categoria":
                    CategoriaDaoImpl ca = new CategoriaDaoImpl();
                    Categoria categoria=new Categoria(nome);
                    ca.insertInto(categoria);
                    break;
            }

            response.sendRedirect("admin?table-select=" + tipo);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date parsedDate = inputFormat.parse(dateString);

        return outputFormat.parse(outputFormat.format(parsedDate));
    }

    private static void error(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws IOException, ServletException {
        List<String> errors = new ArrayList<>();
        errors.add(errorMessage);
        errors.addAll(badInput);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher("/WEB-INF/results/public/error.jsp").forward(request, response);
    }

    private boolean validateInputs(String name, String surname, String address, String nation, String birthDate,
                                   String username, String email, String phoneNumber) {
        if (name == null || name.isEmpty()) return false;
        if (surname == null || surname.isEmpty()) return false;
        if (address == null || address.isEmpty()) return false;
        if (nation == null || nation.isEmpty()) return false;
        if (birthDate == null || birthDate.isEmpty()) return false;
        if (username == null || username.isEmpty()) return false;
        if (email == null || email.isEmpty() || !isValidEmail(email)) {
            badInput.add("Email inserita in modo errato");
            return false;
        }
        if (phoneNumber == null || phoneNumber.isEmpty() || !isValidPhone(phoneNumber)) {
            badInput.add("Numero di telefono inserito in modo errato");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }

    private boolean isValidPhone(String phoneNumber) {
        String phoneRegex = "^\\+[0-9]{2} [0-9]{3} [0-9]{3} [0-9]{4}$";
        String phoneRegex2 = "^(\\d{10})$";
        return phoneNumber != null && (phoneNumber.matches(phoneRegex) || phoneNumber.matches(phoneRegex2));
    }

    private boolean usernameAlreadyUsed(String username){
        return new UtenzaDaoImpl().findByUsername(username) != null;
    }

    private boolean emailAlreadyUsed(String email){
        return new UtenzaDaoImpl().findByEmail(email) != null;
    }
}
