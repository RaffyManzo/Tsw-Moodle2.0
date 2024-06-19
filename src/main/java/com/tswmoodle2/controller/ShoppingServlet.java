package com.tswmoodle2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.beans.Corso;
import model.dao.CorsoDaoImpl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "shoppingServlet", urlPatterns = "/shop")
public class ShoppingServlet extends HttpServlet {

    private Map<Integer, Corso> products;
    private static final String KEY_CART = "cart";
    private static final String CART_PAGE = "/WEB-INF/results/public/cart.jsp";
    private static final Logger LOGGER = Logger.getLogger(ShoppingServlet.class.getName());

    @Override
    public void init() {
        products = new ConcurrentHashMap<>();
        for (Corso c : new CorsoDaoImpl().getAllCourses()) {
            products.put(c.getIdCorso(), c);
        }
        LOGGER.log(Level.INFO, "Products loaded: {0}", products.size());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) {
            action = "viewCart";
        }
        LOGGER.log(Level.INFO, "Action: {0}", action);

        switch (action) {
            case "addToCart":
                addToCart(req, resp);
                break;
            case "removeFromCart":
                removeFromCart(req, resp);
                break;
            case "empty":
                emptyCart(req, resp);
                break;
            case "decreaseQuantity":
                decreaseQuantity(req,resp);
                break;
            case "viewCart":
            default:
                viewCart(req, resp);
                break;
        }
    }

    private void emptyCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute(KEY_CART);
            LOGGER.info("Cart emptied.");
            // Aggiorna il conteggio degli oggetti nel carrello
            updateCartCount(session);
        }
        display(req,resp);
    }

    @SuppressWarnings("unchecked")
    private void viewCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        Map<Corso, Integer> cart = (Map<Corso, Integer>) session.getAttribute(KEY_CART);
        req.setAttribute(KEY_CART, cart);
        LOGGER.log(Level.INFO, "Cart viewed. Cart size: {0}", cart != null ? cart.size() : 0);
        req.getRequestDispatcher(CART_PAGE).forward(req, resp);
    }

    @SuppressWarnings("unchecked")
    private void removeFromCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String productIdParam = req.getParameter("productId");
        if (productIdParam != null) {
            Integer productId = Integer.parseInt(productIdParam);

            HttpSession session = req.getSession(false);
            if (session != null) {
                Map<Corso, Integer> cart = (Map<Corso, Integer>) session.getAttribute(KEY_CART);
                if (cart != null) {
                    Corso product = products.get(productId);
                    if (product != null) {
                        cart.remove(product);
                        session.setAttribute(KEY_CART, cart);
                        LOGGER.log(Level.INFO, "Product removed from cart: {0}", product.getNome());
                    }
                }
                // Aggiorna il conteggio degli oggetti nel carrello
                updateCartCount(session);
            }

        }
        display(req,resp);
    }

    @SuppressWarnings("unchecked")
    private void addToCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String productIdParam = req.getParameter("productId");
        if (productIdParam != null) {
            Integer productId = Integer.parseInt(productIdParam);

            HttpSession session = req.getSession(true);
            Map<Corso, Integer> cart = (Map<Corso, Integer>) session.getAttribute(KEY_CART);
            if (cart == null) {
                cart = new ConcurrentHashMap<>();
                session.setAttribute(KEY_CART, cart);
            }

            Corso product = products.get(productId);
            if (product != null) {
                cart.put(product, cart.getOrDefault(product, 0) + 1);
                session.setAttribute(KEY_CART, cart);
                LOGGER.log(Level.INFO, "Product added to cart: {0}", product);
            }
            // Aggiorna il conteggio degli oggetti nel carrello
            updateCartCount(session);
        }
        display(req,resp);

    }

    @SuppressWarnings("unchecked")
    private void decreaseQuantity(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String productIdParam = req.getParameter("productId");
        if (productIdParam != null) {
            Integer productId = Integer.parseInt(productIdParam);

            HttpSession session = req.getSession();
            if (session.getAttribute(KEY_CART) == null) {
                return;
            }

            Map<Corso, Integer> cart = (Map<Corso, Integer>) session.getAttribute(KEY_CART);
            Corso product = products.get(productId);

            if (cart.containsKey(product)) {
                int quantity = cart.get(product);
                if (quantity > 1) {
                    cart.put(product, quantity - 1);
                } else {
                    cart.remove(product);
                }
            }

            session.setAttribute(KEY_CART, cart);
            // Aggiorna il conteggio degli oggetti nel carrello
            updateCartCount(session);
        }
        display(req,resp);
    }

    private void display(HttpServletRequest req, HttpServletResponse resp) throws IOException  {
        // Usa l'header Referer per determinare la pagina di origine
        String referer = req.getHeader("Referer");

        if (referer != null && referer.contains("cart")) {
            // Se il Referer contiene "cart", aggiorna la pagina del carrello
            resp.sendRedirect("shop?action=viewCart");
        } else {
            // Altrimenti, reindirizza al Referer
            resp.sendRedirect(referer);
        }
    }

    private void updateCartCount(HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Corso, Integer> cart = (Map<Corso, Integer>) session.getAttribute(KEY_CART);
        int itemCount = 0;
        if (cart != null) {
            for (int quantity : cart.values()) {
                itemCount += quantity;
            }
        }
        session.setAttribute("cartItemCount", itemCount);
    }
}
