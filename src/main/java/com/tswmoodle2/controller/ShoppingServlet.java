package com.tswmoodle2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.beans.Corso;
import model.beans.Utenza;
import model.dao.CategoriaDaoImpl;
import model.dao.CorsoDaoImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "shoppingServlet", urlPatterns = "/shop")
public class
ShoppingServlet extends HttpServlet {

    private Map<Integer, Corso> products;
    private static final String ERROR_VIEW = "/WEB-INF/results/public/error.jsp";

    private static final String KEY_CART = "cart";
    private static final String CART_PAGE = "/WEB-INF/results/public/cart.jsp";
    private static final Logger LOGGER = Logger.getLogger(ShoppingServlet.class.getName());
    Utenza user;

    @Override
    public void init() {
        products = new ConcurrentHashMap<>();
        for (Corso c : new CorsoDaoImpl().getAllCourses()) {
            products.put(c.getIdCorso(), c);
        }
        LOGGER.log(Level.INFO, "Products loaded: {0}", products.size());


    }

    private void updateCart() {
        for (Corso c : new CorsoDaoImpl().getAllCourses()) {
            products.put(c.getIdCorso(), c);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        updateCart();
        HttpSession session = req.getSession(false);
        user = session != null ? (Utenza) session.getAttribute("user") : null;

        if(user != null) {
            if (!user.getTipo().equals("S")) {
                req.setAttribute("errors", new ArrayList<>(List.of("Questo account non puó accedere a questa funzione")));
                req.getRequestDispatcher(ERROR_VIEW).forward(req, resp);
            }
        }

        String action = req.getParameter("action");
        if (action == null) {
            action = "viewCart";
        }
        LOGGER.log(Level.INFO, "Action: {0}", action);

        req.setAttribute("categories", new CategoriaDaoImpl().getAllCategorie());


        switch (action) {
            case "addToCart":
                try {
                    addToCart(req, resp);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "removeFromCart":
                try {
                    removeFromCart(req, resp);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "empty":
                try {
                    emptyCart(req, resp);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "decreaseQuantity":
                try {
                    decreaseQuantity(req, resp);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "viewCart":
            default:
                viewCart(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
    private void emptyCart(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute(KEY_CART);
            LOGGER.info("Carrello emptied.");
            updateCartCount(session);
        }
        display(req, resp);
    }

    @SuppressWarnings("unchecked")
    private void viewCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        Map<Corso, Integer> cart = (Map<Corso, Integer>) session.getAttribute(KEY_CART);
        req.setAttribute(KEY_CART, cart);
        LOGGER.log(Level.INFO, "Carrello viewed. Carrello size: {0}", cart != null ? cart.size() : 0);
        req.getRequestDispatcher(CART_PAGE).forward(req, resp);
    }

    @SuppressWarnings("unchecked")
    private void removeFromCart(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
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
                updateCartCount(session);
            }
        }
        display(req, resp);
    }

    @SuppressWarnings("unchecked")
    private void addToCart(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
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


                cart.put(product, 1);
                session.setAttribute(KEY_CART, cart);
                LOGGER.log(Level.INFO, "Product added to cart: {0}", product.getNome());

            }
            updateCartCount(session);
        }
        display(req, resp);
    }

    @SuppressWarnings("unchecked")
    private void decreaseQuantity(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
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
                session.setAttribute(KEY_CART, cart);

            }
            updateCartCount(session);
        }
        display(req, resp);
    }

    private void display(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String referer = req.getHeader("Referer");

        if (referer != null && referer.contains("cart")) {
            resp.sendRedirect("shop?action=viewCart");
        } else {
            resp.sendRedirect(referer);
        }
    }

    protected void updateCartCount(HttpSession session) {
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