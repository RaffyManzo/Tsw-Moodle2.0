package model.dao;

import model.beans.Argomento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;


public class ArgomentoDaoImpl extends AbstractDataAccessObject<Argomento> implements ArgomentoDao{
    @Override
    public Argomento insertInto(Argomento argomento) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "INSERT_ARGOMENTO")) {
            ps.setDate(1, new java.sql.Date(argomento.getDataCaricamento().getTime()));
            ps.setString(2, argomento.getNome());
            ps.setString(3, argomento.getDescrizione());
            ps.setInt(4, argomento.getId());

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
        return argomento;
    }

    @Override
    public void update(Argomento argomento) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "UPDATE_ARGOMENTO")) {
            ps.setDate(1, new java.sql.Date(argomento.getDataCaricamento().getTime()));
            ps.setString(2, argomento.getNome());
            ps.setString(3, argomento.getDescrizione());
            ps.setInt(4, argomento.getId());
            ps.setInt(5, argomento.getId());
            ps.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "DELETE_ARGOMENTO")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Argomento findById(int id) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_ARGOMENTO_BY_ID")) {
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

    public ArrayList<Argomento> findByNome(String nome) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_ARGOMENTO_BY_NOME")) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return getResultAsList(rs);
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public ArrayList<Argomento> findAllByLezioneId(int idLezione) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_ALL_ARGOMENTI_BY_LEZIONE_ID")) {
            ps.setInt(1, idLezione);
            try (ResultSet rs = ps.executeQuery()) {
                return getResultAsList(rs);
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public int countAllByCorsoId(int idCorso) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_ALL_ARGOMENTI_COUNT_BY_CORSO_ID")) {
            ps.setInt(1, idCorso);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return 0;
    }

    @Override
    protected Argomento extractFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        Date dataCaricamento = rs.getDate("dataCaricamento");
        String nome = rs.getString("nome");
        String descrizione = rs.getString("descrizione");
        int idLezione = rs.getInt("idLezione");
        return new Argomento(id, dataCaricamento, nome, descrizione, idLezione);
    }
}
