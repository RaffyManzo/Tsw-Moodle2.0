package model.dao;

import model.beans.Carrello;
import model.beans.Corso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

        return new Carrello(courses, quantities, idStudente, idCarrello);
    }

    @Override
    protected Carrello insertInto(Carrello carrello) throws SQLException {

        return null;
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
     * @param IDCorso    ID del corso da rimuovere
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

        public void deleteCarrello(int IDCarrello) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "DELETE_CART")) {
            ps.setInt(1, IDCarrello);
            ps.executeUpdate();
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
    public void clearCart(int idCarrello) {
        try (Connection conn = getConnection();
             PreparedStatement deleteStmt = prepareStatement(conn, "CLEAR_CART")) {
            deleteStmt.setInt(1, idCarrello);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveOrUpdateCarrello(Carrello carrello) {
        LOGGER.log(Level.INFO, "Saving or updating cart: IDCarrello: {0}, IDUtente: {1}", new Object[]{carrello.getIDCarrello(), carrello.getIDUtente()});
        try {
            Connection conn = null;
            try {
                conn = getConnection();
                conn.setAutoCommit(false);

                ArrayList<Corso> corsiInDB = new ArrayList<>(
                        getCartByUserID(carrello.getIDUtente()).getCart().keySet()
                );

                // Per ogni corso nel carrello
                for (Corso corso : carrello.getCart().keySet()) {
                    if (corsiInDB.contains(corso)) {
                        // Se il corso è già nel DB, non fare nulla
                        corsiInDB.remove(corso);
                    } else {
                        // Se il corso non è nel DB, inseriscilo
                        String insertQuery = "INSERT_CART_ELEMENT";
                        try (PreparedStatement insertStmt = prepareStatement(conn, insertQuery)) {
                            insertStmt.setInt(1, corso.getIdCorso());
                            insertStmt.setInt(2, carrello.getIDCarrello());
                            insertStmt.setInt(3, carrello.getIDUtente());
                            insertStmt.executeUpdate();
                        }
                    }
                }

                // Elimina gli elementi non presenti nel carrello dal database
                for (Corso corso : corsiInDB) {
                    deleteFromCarrello(carrello.getIDCarrello(), carrello.getIDUtente(), corso.getIdCorso());
                }

                conn.commit();
            } catch (SQLException ex) {
                if (conn != null) {
                    conn.rollback();
                }
                throw ex;
            } finally {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public int createCartForUser(int userID) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = prepareStatement(conn, "INSERT_CART", PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userID);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.getMetaData().getColumnCount() == 1) {
                    if(rs.next())
                        return rs.getInt(1);
                } else {
                    throw new SQLException("Creating carrello failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }


    public int getCartIDByUser(int IDUtente) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
}
