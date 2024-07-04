package model.dao;

import model.beans.Argomento;
import model.beans.Lezione;

import java.sql.*;
import java.util.ArrayList;

public class LezioneDaoImpl extends AbstractDataAccessObject<Lezione> implements LezioneDao{
    @Override
    public Lezione insertInto(Lezione lezione) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "INSERT_LEZIONE", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, lezione.getDescrizione());
            ps.setString(2, lezione.getTitolo());
            ps.setInt(3, lezione.getIdCorso());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return getResultAsObject(rs);
                    }
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return lezione;
    }

    @Override
    public void update(Lezione lezione) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "UPDATE_LEZIONE")) {
            ps.setString(1, lezione.getDescrizione());
            ps.setString(2, lezione.getTitolo());
            ps.setInt(3, lezione.getIdCorso());
            ps.setInt(4, lezione.getId());

            ps.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "DELETE_LEZIONE")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Lezione findById(int id) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_LEZIONE_BY_ID")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractFromResultSet(rs);
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return null;
    }

    public ArrayList<Lezione> findByTitolo(String titolo) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_LEZIONE_BY_TITOLO")) {
            ps.setString(1, "%" + titolo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return getResultAsList(rs);
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public ArrayList<Lezione> findAllByCorsoId(int idCorso) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_ALL_LEZIONI_BY_CORSO_ID")) {
            ps.setInt(1, idCorso);
            try (ResultSet rs = ps.executeQuery()) {
                return getResultAsList(rs);
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    protected Lezione extractFromResultSet(ResultSet rs) throws SQLException {

        if (rs.getMetaData().getColumnCount() == 1) {
            return new Lezione(rs.getInt(1), null, null, 0, null);
        } else {
            int id = rs.getInt("id");
            String descrizione = rs.getString("descrizione");
            String titolo = rs.getString("titolo");
            int idCorso = rs.getInt("idCorso");
            return new Lezione(id, descrizione, titolo, idCorso, new ArrayList<Argomento>());
        }
    }
}
