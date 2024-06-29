package com.tswmoodle2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.beans.Utenza;
import model.dao.UtenzaDaoImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@WebServlet(name = "RegistrationServlet", value = "/new")
public class RegistrationServlet extends HttpServlet {
    private static ArrayList<String> badInput = new ArrayList<>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountType = request.getParameter("account-type");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String address = request.getParameter("address");
        String nation = request.getParameter("nation");
        String birthDate = request.getParameter("birth-date");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String countryCode = request.getParameter("countryCode");
        String phoneNumber = request.getParameter("phoneNumber");
        String password = request.getParameter("password");
        String passwordCheck = request.getParameter("password-check");

        // Validate inputs
        if (!validateInputs(name, surname, address, nation, birthDate, username, email, countryCode, phoneNumber, password, passwordCheck)) {
            error(request, response, "Alcuni campi sono vuoti o inseriti in modo errato");
            return;
        }

        if(usernameAlreadyUsed(username)) {
            error(request, response, "Username giá in uso");
            return;
        }

        if(emailAlreadyUsed(email)) {
            error(request, response, "Email giá in uso");
            return;
        }

        try {
            // Ottieni la data corrente formattata come "dd/MM/yyyy"
            // Ottieni la data corrente formattata come "dd/MM/yyyy"
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String todayStr = sdf.format(today.getTime());

            // Converti le date usando il formato corretto
            String birthDateFormatted = reformatDate(birthDate, "yyyy-MM-dd", "dd/MM/yyyy");

            // Stampa le date formattate
            System.out.println(birthDateFormatted);
            System.out.println(todayStr);

            // Converte le stringhe di data in oggetti Date
            Date birthDateObj = convertStringToDate(birthDateFormatted, "dd/MM/yyyy");
            Date todayObj = convertStringToDate(todayStr, "dd/MM/yyyy");




        // Create Utenza object and insert it into the database
            Utenza utenza = new Utenza(
                    0, name, surname, birthDateObj,
                    address, nation, countryCode + " " + phoneNumber, email, toHash(password),
                    todayObj, username, accountType, ""
            );

            UtenzaDaoImpl utenzaDao = new UtenzaDaoImpl();
            boolean success = utenzaDao.insertInto(utenza);

            if (success) {
                request.setAttribute("operation", "registrazione");
                request.setAttribute("message-header", "Benvenuto " + name);
                List<String> msgs = new ArrayList<>();
                msgs.add("Il tuo account é stato creato in modo corretto");
                msgs.add("Per accedere all'area riservata effettua il login");
                request.setAttribute("messages", msgs);

                request.getRequestDispatcher("/WEB-INF/results/public/success.jsp").forward(request, response);
            } else {
                error(request, response, "La registrazione <strong>NON</strong> andata a buon fine");

            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public static Date convertStringToDate(String dateStr, String formatStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.parse(dateStr);
    }

    public static String reformatDate(String date, String inputFormatStr, String outputFormatStr) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputFormatStr);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputFormatStr);

        // Parsing della stringa di input nel formato di ingresso
        Date parsedDate = inputFormat.parse(date);

        // Formattazione della data nel formato di uscita e ritorno della data riformattata e convertita
        return outputFormat.format(parsedDate);
    }


    private static void error(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws IOException, ServletException {
        List<String> errors = new ArrayList<>();
        errors.add(errorMessage);
        errors.addAll(badInput);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher("/WEB-INF/results/public/error.jsp").forward(request, response);
    }
    private boolean validateInputs(String name, String surname, String address, String nation, String birthDate,
                                   String username, String email, String countryCode, String phoneNumber,
                                   String password, String passwordCheck) {
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
        if (countryCode == null || countryCode.isEmpty()) return false;
        if (phoneNumber == null || phoneNumber.isEmpty() || !isValidPhone(phoneNumber)) {
            badInput.add("Numero di telefono inserito in modo errato");
            return false;
        }
        if (password == null || password.isEmpty() || !isValidPassword(password)) {
            badInput.add("Password inserita in modo errato");
            return false;
        }
        if (!password.equals(passwordCheck)) {
            badInput.add("Le password non corrispondono");
            return false;
        }
        return true;
    }

    private boolean usernameAlreadyUsed(String username){
        return new UtenzaDaoImpl().findByUsername(username) != null;
    }

    private boolean emailAlreadyUsed(String email){
        return new UtenzaDaoImpl().findByEmail(email) != null;
    }

    private boolean isValidPhone(String phoneNumber) {
        String phoneRegex = "^[0-9]{3} [0-9]{3} [0-9]{4}$";
        return phoneNumber != null && phoneNumber.matches(phoneRegex);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8 && password.length() <= 15
                && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*")
                && password.matches(".*[0-9].*") && password.matches(".*[!@#$%^&*()].*");
    }


    private String toHash(String password) {
        StringBuilder hashString = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            for (byte b : hash) {
                hashString.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e);
        }
        return hashString.toString();
    }
}
