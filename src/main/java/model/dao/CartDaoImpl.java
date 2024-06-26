package model.dao;

import com.tswmoodle2.controller.ShoppingServlet;
import model.beans.Carrello;
import model.beans.Corso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartDaoImpl extends AbstractDataAccessObject<Carrello> implements CartDao {
    private static final Logger LOGGER = Logger.getLogger(CartDaoImpl.class.getName());

    @Override
    protected Carrello extractFromResultSet(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }

        int idCarrello = rs.getInt("IDCarrello");
        int idStudente = rs.getInt("IDUtente");

        ArrayList<Corso> courses = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();

        do {
            int idCorso = rs.getInt("IDCorso");
            int quantity = rs.getInt("Quantita");

            Corso corso = new CorsoDaoImpl().findByID(idCorso);
            courses.add(corso);
            quantities.add(quantity);
        } while (rs.next());

        return new Carrello(courses, quantities, idCarrello, idStudente);
    }

    @Override
    protected boolean insertInto(Carrello carrello) throws SQLException {

        return false;
    }

    public Carrello getCartByUserID(int userID) {
        try (Connection conn = getConnection()) {
            try (PreparedStatement ps = prepareStatement(conn, "GET_CART_BY_USER_ID")) {
                ps.setInt(1, userID);
                return getResultAsObject(ps.executeQuery());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void update(Carrello carrello) {

    }


    /**
     * Rimuove un elemento specifico dal carrello.
     *
     * @param IDCarrello ID del carrello
     * @param IDCorso ID del corso da rimuovere
     * @throws SQLException
     */
    @Override
    public void deleteFromCarrello(int IDCarrello, int IDUtente, int IDCorso) {
        try (Connection conn = getConnection()) {
            try (PreparedStatement deleteStmt = prepareStatement(conn, "DELETE_FROM_CART")) {
                deleteStmt.setInt(1, IDCarrello);
                deleteStmt.setInt(2, IDUtente);
                deleteStmt.setInt(3, IDCorso);
                deleteStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Svuota completamente il carrello.
     *
     * @param idCarrello ID del carrello da svuotare
     * @throws SQLException
     */
    public void clearCart(int idCarrello) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement deleteStmt = prepareStatement(conn, "CLEAR_CART")) {
            deleteStmt.setInt(1, idCarrello);
            deleteStmt.executeUpdate();
        }
    }

    @Override
    public void saveOrUpdateCarrello(Carrello carrello) {
        LOGGER.log(Level.INFO, "Saving or updating cart: IDCarrello: {0}, IDUtente: {1}", new Object[]{carrello.getIDCarrello(), carrello.getIDUtente()});

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                for (Map.Entry<Corso, Integer> entry : carrello.getCart().entrySet()) {
                    Corso corso = entry.getKey();
                    int quantita = entry.getValue();

                    String selectQuery = "GET_CART_ELEMENT";
                    try (PreparedStatement selectStmt = prepareStatement(conn, selectQuery)) {
                        selectStmt.setInt(1, corso.getIdCorso());
                        selectStmt.setInt(2, carrello.getIDCarrello());
                        selectStmt.setInt(3, carrello.getIDUtente());

                        ResultSet rs = selectStmt.executeQuery();

                        if (rs.next()) {
                            String updateQuery = "UPDATE_CART_ELEMENT";
                            try (PreparedStatement updateStmt = prepareStatement(conn, updateQuery)) {
                                updateStmt.setInt(1, quantita);
                                updateStmt.setInt(2, corso.getIdCorso());
                                updateStmt.setInt(3, carrello.getIDCarrello());
                                updateStmt.setInt(4, carrello.getIDUtente());
                                updateStmt.executeUpdate();
                            }
                        } else {
                            String insertQuery = "INSERT_CART_ELEMENT";
                            try (PreparedStatement insertStmt = prepareStatement(conn, insertQuery)) {
                                insertStmt.setInt(1, corso.getIdCorso());
                                insertStmt.setInt(2, carrello.getIDCarrello());
                                insertStmt.setInt(3, carrello.getIDUtente());
                                insertStmt.setInt(4, quantita);
                                insertStmt.executeUpdate();
                            }
                        }
                    }
                }
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }






    public int createCartForUser(int userID) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = prepareStatement(conn, "INSERT_CART", PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userID);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating carrello failed, no ID obtained.");
                }
            }
        }
    }


    public int getCartIDByUser(int IDUtente) throws SQLException {
        try (Connection conn = getConnection()) {
            try (PreparedStatement ps = prepareStatement(conn, "GET_CART_ID_BY_USER")) {
                ps.setInt(1, IDUtente);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(
                            "IDCarrello"
                    );
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return -1;
    }
}
