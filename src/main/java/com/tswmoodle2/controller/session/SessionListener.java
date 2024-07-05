package com.tswmoodle2.controller.session;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import model.Util.CarrelloService;
import model.beans.Carrello;
import model.beans.Corso;
import model.beans.Utenza;
import model.dao.CartDaoImpl;

import java.util.Map;

@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setMaxInactiveInterval(900); // 15 minuti
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        Utenza user = (Utenza) session.getAttribute("user");

        if (user != null) {
            Carrello carrello = new CarrelloService().getOrCreateCarrello(user);
            carrello.setCart((Map<Corso, Integer>) session.getAttribute("cart"));

            CartDaoImpl cartDao = new CartDaoImpl();
            cartDao.saveOrUpdateCarrello(carrello);
        }
    }
}
