package model.dao;

import model.beans.CreditCard;

import java.sql.*;

public class CreditCardDaoImpl extends AbstractDataAccessObject<CreditCard> implements CreditCardDao{
    @Override
    protected CreditCard extractFromResultSet(ResultSet rs) throws SQLException {
        return new CreditCard(
                rs.getString("numero"),
                rs.getString("Titolare"),
                rs.getString("dataScadenza"),
                rs.getInt("IDUtente")
        );
    }

    @Override
    public CreditCard insertInto(CreditCard creditCard) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "INSERT_CREDIT_CARD", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, creditCard.getNumeroCarta());
            ps.setString(2, creditCard.getTitolare());
            ps.setString(3, creditCard.getDataScadenza());
            ps.setInt(4, creditCard.getIdUtente());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return getResultAsObject(rs);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return creditCard;
    }

    @Override
    public void update(CreditCard creditCard) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "UPDATE_CREDIT_CARD")) {
            ps.setString(1, creditCard.getNumeroCarta());
            ps.setString(2, creditCard.getTitolare());
            ps.setString(3, creditCard.getDataScadenza());
            ps.setInt(4, creditCard.getIdUtente());

            ps.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public CreditCard findByUserID(int id) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "GET_CREDIT_CARD_BY_USER_ID")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getResultAsObject(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void saveOrUpdateCard(CreditCard card) {
        if(findByUserID(card.getIdUtente()) != null) {
            update(card);
        } else {
            insertInto(card);
        }
    }
}
