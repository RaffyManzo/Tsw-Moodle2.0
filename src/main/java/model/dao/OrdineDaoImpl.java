package model.dao;

import model.DBManager;
import model.beans.Carrello;
import model.beans.Corso;
import model.beans.CreditCard;
import model.beans.Ordine;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdineDaoImpl extends AbstractDataAccessObject<Ordine> implements OrdineDao{
    private static final Logger logger = Logger.getLogger(OrdineDaoImpl.class.getName());
    @Override
    protected Ordine extractFromResultSet(ResultSet rs) throws SQLException {
        if (rs.getMetaData().getColumnCount() == 1) {
            return new Ordine(rs.getInt(1), null, 0, null, 0.0);
        } else {
            return new Ordine(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getInt(3),
                    rs.getDate(4),
                    rs.getDouble(5),
                    rs.getTimestamp(6),
                    UUID.fromString(rs.getString(7))
            );
        }
    }

    @Override
    protected Ordine insertInto(Ordine o) throws SQLException {


        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "ADD_PAYMENT", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, o.getNumeroCarta());
            ps.setInt(2, o.getIdutente());
            ps.setDate(3, new java.sql.Date(o.getDataPagamento().getTime()));
            ps.setDouble(4, o.getTotale());
            ps.setString(5, o.getUuid().toString());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return getResultAsObject(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return o;
    }

    @Override
    protected void update(Ordine e) {

    }

    public Ordine findOrderByID(int id) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "GET_ORDER")) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return getResultAsObject(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void purchaseCourse(int userID, int courseID, int orderID, Connection conn) throws SQLException {
        try (
             PreparedStatement ps = prepareStatement(conn, "PURCHASE_COURSE", Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, courseID);
            ps.setInt(2, userID);
            ps.setInt(3, orderID);


            logger.log(Level.INFO, "Acquisto del corso con ID: " + courseID + " per l'utente con ID: " + userID + " completato");
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante l'acquisto del corso", e);
            throw e;
        }
    }

    private boolean existUUID(UUID uuid) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "CHECK_UUID")) {
            ps.setString(1, uuid.toString());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void purchaseCoursesFromCart(Carrello carrello, CreditCard card) throws SQLException {
        Connection connection = null;
        try  {
            connection = DBManager.getConnection();
            // Start the transaction
            connection.setAutoCommit(false);

            try {
                Ordine order = addPayment(carrello, card);

                if (order == null) {
                    throw new RuntimeException("Ordine gia presente");
                }

                // Purchase each course in the cart
                for (Corso c : carrello.getCart().keySet()) {
                    purchaseCourse(carrello.getIDUtente(), c.getIdCorso(), order.getId(), connection);
                }

                // Clear the cart
                new CartDaoImpl().clearCart(carrello.getIDCarrello());

                // Commit the transaction
                connection.commit();
            } catch (SQLException e) {
                // Rollback the transaction in case of any exception
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException && e.getMessage().contains("Duplicate entry")) {
                String[] parts = e.getMessage().split("'");
                String courseId = String.valueOf(parts[1].charAt(0));
                String userId = parts[1].substring(2);
                logger.log(Level.SEVERE, "Il corso con ID: " + courseId + " è già stato acquistato dall'utente con ID: " + userId, e);
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                throw new SQLException("Il corso con ID: " + courseId + " è già stato acquistato dall'utente con ID: " + userId);
            } else {
                logger.log(Level.SEVERE, "Errore durante la transazione, rollback eseguito", e);
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                throw e;
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected Ordine addPayment(Carrello carrello, CreditCard card) throws SQLException {
        double somma = carrello.getCart().keySet().stream()
                .mapToDouble(Corso::getPrezzo)
                .sum();

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String todayStr = sdf.format(today.getTime());
        Date todayObj = null;

        // Converte le stringhe di data in oggetti Date
        try {
            todayObj = convertStringToDate(todayStr);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
        Ordine o = new Ordine(
                card.getNumeroCarta(),
                card.getIdUtente(),
                null,
                somma
        );
        // Imposta il timestamp e l'UUID
        o.setTimestamp(new Timestamp(System.currentTimeMillis()));
        UUID uuid = UUID.randomUUID();
        o.setUuid(uuid);

        o.setDataPagamento(todayObj);
        o.setCorsiAcquistati(carrello.getCart().keySet());

        if(!existUUID(uuid))
            return insertInto(o);
        else return null;
    }

    private static Date convertStringToDate(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.parse(dateStr);
    }

    private static String reformatDate(String date, String inputFormatStr, String outputFormatStr) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputFormatStr);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputFormatStr);

        // Parsing della stringa di input nel formato di ingresso
        Date parsedDate = inputFormat.parse(date);

        // Formattazione della data nel formato di uscita e ritorno della data riformattata e convertita
        return outputFormat.format(parsedDate);
    }
}
