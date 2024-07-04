package com.tswmoodle2.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Util.CarrelloScheduler;
import model.Util.CarrelloService;
import model.beans.Carrello;
import model.beans.Corso;
import model.beans.Utenza;
import model.dao.CartDaoImpl;
import model.dao.CorsoDaoImpl;

import java.io.IOException;
import java.sql.SQLException;
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
    Utenza user;

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

        HttpSession session = req.getSession(false);
        user = session != null ? (Utenza) session.getAttribute("user") : null;


        String action = req.getParameter("action");
        if (action == null) {
            action = "viewCart";
        }
        LOGGER.log(Level.INFO, "Action: {0}", action);

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
    private void emptyCart(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, ServletException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute(KEY_CART);
            LOGGER.info("Carrello emptied.");
            updateCartCount(session);

            if(user != null) {
                new CartDaoImpl().clearCart(
                        new CartDaoImpl().getCartIDByUser(user.getIdUtente())
                );
            }
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
    private void removeFromCart(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, ServletException {
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

                        if(user != null) {
                            new CartDaoImpl().deleteFromCarrello(
                                    new CartDaoImpl().getCartIDByUser(user.getIdUtente()),
                                    user.getIdUtente(),
                                    productId
                            );
                        }
                    }
                }
                updateCartCount(session);
            }
        }
        display(req, resp);
    }

    @SuppressWarnings("unchecked")
    private void addToCart(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, ServletException {
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

                if(user != null) {
                    Carrello carrello = new Carrello((Map<Corso, Integer>) session.getAttribute(KEY_CART),
                            user.getIdUtente(),
                            new CartDaoImpl().getCartIDByUser(user.getIdUtente()));
                    new CarrelloService().saveCarrello(carrello);
                }
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

                if(user != null) {
                    Carrello carrello = new Carrello((Map<Corso, Integer>) session.getAttribute(KEY_CART),
                            user.getIdUtente(),
                            new CartDaoImpl().getCartIDByUser(user.getIdUtente()));
                    new CarrelloService().saveCarrello(carrello);
                }

            }
            updateCartCount(session);
        }
        display(req, resp);
    }

    private void display(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String admin = req.getParameter("admin");
        if(admin.equals("true")) {
            String elemento=req.getParameter("id");
            req.setAttribute("elemento", elemento);
            RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/results/admin/modificaCarrello.jsp");
            try {
                rd.forward(req, resp);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }

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
