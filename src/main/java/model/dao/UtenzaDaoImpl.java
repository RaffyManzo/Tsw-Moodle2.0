package model.dao;

import model.beans.Utenza;

import java.sql.*;
import java.util.ArrayList;

public class UtenzaDaoImpl extends AbstractDataAccessObject<Utenza> implements UtenzaDao {

    ArrayList<Utenza> users = new ArrayList<>();

    @Override
    public boolean insertInto(Utenza utenza) {
        boolean success = false;
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "INSERT INTO Utenza (Nome, Cognome, DataNascita, Indirizzo, Citta, Telefono, Email, Password, DataCreazioneAccount, Username, Tipo, Immagine) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING IDUtente")) {
            ps.setString(1, utenza.getNome());
            ps.setString(2, utenza.getCognome());
            ps.setDate(3, new java.sql.Date(utenza.getDataNascita().getTime()));
            ps.setString(4, utenza.getIndirizzo());
            ps.setString(5, utenza.getCitta());
            ps.setString(6, utenza.getTelefono());
            ps.setString(7, utenza.getEmail());
            ps.setString(8, utenza.getPassword());
            ps.setDate(9, new java.sql.Date(utenza.getDataCreazioneAccount().getTime()));
            ps.setString(10, utenza.getUsername());
            ps.setString(11, utenza.getTipo());
            ps.setString(12, utenza.getImg());

            int rowsAffected = ps.executeUpdate();
            success = rowsAffected > 0; // Se l'operazione ha avuto successo, rowsAffected sarà maggiore di 0

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return success;
    }

    @Override
    public void update(Utenza utenza) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "UPDATE_UTENZA")) {
            ps.setString(1, utenza.getNome());
            ps.setString(2, utenza.getCognome());
            ps.setDate(3, utenza.getDataNascita());
            ps.setString(4, utenza.getIndirizzo());
            ps.setString(5, utenza.getCitta());
            ps.setString(6, utenza.getTelefono());
            ps.setString(7, utenza.getEmail());
            ps.setString(8, utenza.getPassword());
            ps.setDate(9, utenza.getDataCreazioneAccount());
            ps.setString(10, utenza.getUsername());
            ps.setString(11, utenza.getTipo());
            ps.setString(12, utenza.getImg());
            ps.setInt(13, utenza.getIdUtente());
            ps.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }



    @Override
    public void delete(int id) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "DELETE_UTENZA")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Utenza> getAllUsers() {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_ALL_UTENZA");
             ResultSet rs = ps.executeQuery()) {
            users.addAll(getResultAsList(rs));
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Utenza findByID(int id) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_UTENZA_BY_ID")) {
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
    public ArrayList<Utenza> findByNome(String nome) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_UTENZA_BY_NOME")) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                return getResultAsList(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Utenza> findByCognome(String cognome) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_UTENZA_BY_COGNOME")) {
            ps.setString(1, cognome);
            try (ResultSet rs = ps.executeQuery()) {
                return getResultAsList(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Utenza> findByDateRange(Date startDate, Date endDate) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_UTENZA_BY_DATE_RANGE")) {
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
    public Utenza findByEmail(String email) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_UTENZA_BY_EMAIL")) {
            ps.setString(1, email);


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
    public Utenza findByUsername(String username) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_UTENZA_BY_USERNAME")) {
            ps.setString(1, username);

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
    public ArrayList<Utenza> findByTipo(String tipo) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_UTENZA_BY_TIPO")) {
            ps.setString(1, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getResultAsList(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public ArrayList<Utenza> findByTelefono(String telefono) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_UTENZA_BY_TELEFONO")) {
            ps.setString(1, telefono);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getResultAsList(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public ArrayList<Utenza> findByCitta(String citta) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_UTENZA_BY_CITTA")) {
            ps.setString(1, citta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getResultAsList(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected Utenza extractFromResultSet(ResultSet rs) throws SQLException {
        int idUtente = rs.getInt("IDUtente");
        String nome = rs.getString("Nome");
        String cognome = rs.getString("Cognome");
        Date dataNascita = rs.getDate("DataNascita");
        String indirizzo = rs.getString("Indirizzo");
        String citta = rs.getString("Citta");
        String telefono = rs.getString("Telefono");
        String email = rs.getString("Email");
        String password = rs.getString("Password");
        Date dataCreazioneAccount = rs.getDate("DataCreazioneAccount");
        String username = rs.getString("Username");
        String tipo = rs.getString("Tipo");
        String img = rs.getString("Immagine");

        return new Utenza(idUtente, nome, cognome, dataNascita, indirizzo, citta, telefono, email, password, dataCreazioneAccount, username, tipo, img);
    }
}
