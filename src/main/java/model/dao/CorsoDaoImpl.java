package model.dao;

import model.beans.Corso;

import java.sql.*;
import java.util.ArrayList;

public class CorsoDaoImpl extends AbstractDataAccessObject<Corso> implements CorsoDao{

    ArrayList<Corso> courses = new ArrayList<>();
    @Override
    public void insertInto(Corso corso)  {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "INSERT_CORSO")) {
            ps.setString(1, corso.getNomeCategoria());
            ps.setString(2, corso.getNome());
            ps.setString(3, corso.getDescrizione());
            ps.setString(4, corso.getImmagine());
            ps.setString(5, corso.getCertificazione());
            ps.setDate(6, corso.getDataCreazione());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <E> void update(E e)  {
        Corso corso = (Corso) e;
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection,"UPDATE_CORSO")) {
            ps.setString(1, corso.getNomeCategoria());
            ps.setString(2, corso.getNome());
            ps.setString(3, corso.getDescrizione());
            ps.setString(4, corso.getImmagine());
            ps.setString(5, corso.getCertificazione());
            ps.setDate(6, corso.getDataCreazione());
            ps.setInt(7, corso.getIdCorso());
            ps.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "DELETE_CORSO")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Corso> getAllCourses()  {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_ALL_CORSI");
             ResultSet rs = ps.executeQuery()) {
            courses.addAll(getResultAsList(rs));
            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Corso findByID(int id)  {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_CORSO_BY_ID")) {
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
    public ArrayList<Corso> findByCategoria(String categoria)  {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_CORSI_BY_CATEGORIA")) {
            ps.setString(1, categoria);
            try (ResultSet rs = ps.executeQuery()) {
                return getResultAsList(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Corso> searchByName(String nome)  {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "SEARCH_CORSI_BY_NOME")) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return getResultAsList(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Corso> findByDateRange(Date startDate, Date endDate)  {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_CORSI_BY_DATE_RANGE")) {
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                return getResultAsList(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Corso> findByCertificazione(String certificazione)  {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_CORSI_BY_CERTIFICAZIONE")) {
            ps.setString(1, certificazione);
            try (ResultSet rs = ps.executeQuery()) {
                return getResultAsList(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Corso extractFromResultSet(ResultSet rs) throws SQLException {
        int idCorso = rs.getInt("IDCorso");
        String nomeCategoria = rs.getString("NomeCategoria");
        String nome = rs.getString("Nome");
        String descrizione = rs.getString("Descrizione");
        String immagine = rs.getString("Immagine");
        String certificazione = rs.getString("Certificazione");
        java.sql.Date dataCreazione = rs.getDate("DataCreazione");

        return new Corso(idCorso, nomeCategoria, nome, descrizione, immagine, certificazione, dataCreazione);
    }
}
