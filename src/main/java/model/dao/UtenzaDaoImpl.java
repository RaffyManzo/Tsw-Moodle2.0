package model.dao;

import model.beans.Utenza;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UtenzaDaoImpl extends AbstractDataAccessObject<Utenza> implements UtenzaDao {

    ArrayList<Utenza> users = new ArrayList<>();


    @Override
    public Utenza insertInto(Utenza utenza) {
        boolean success = false;
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "INSERT_UTENZA", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, utenza.getNome());
            ps.setString(2, utenza.getCognome());
            ps.setDate(3,
                    new java.sql.Date(utenza.getDataNascita().getTime()));
            ps.setString(4, utenza.getIndirizzo());
            ps.setString(5, utenza.getNazione());
            ps.setString(6, utenza.getTelefono());
            ps.setString(7, utenza.getEmail());
            ps.setString(8, utenza.getPassword());
            ps.setDate(9,
                    new java.sql.Date(utenza.getDataCreazioneAccount().getTime()));
            ps.setString(10, utenza.getUsername());
            ps.setString(11, utenza.getTipo());
            ps.setString(12, utenza.getImg());


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
        return utenza;
    }

    @Override
    public void update(Utenza utenza) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "UPDATE_UTENZA")) {
            ps.setString(1, utenza.getNome());
            ps.setString(2, utenza.getCognome());
            ps.setDate(3,
                    new java.sql.Date(utenza.getDataNascita().getTime()));
            ps.setString(4, utenza.getIndirizzo());
            ps.setString(5, utenza.getNazione());
            ps.setString(6, utenza.getTelefono());
            ps.setString(7, utenza.getEmail());
            ps.setString(8, utenza.getPassword());
            ps.setDate(9,
                    new java.sql.Date(utenza.getDataCreazioneAccount().getTime()));

            ps.setString(10, utenza.getUsername());
            ps.setString(11, utenza.getTipo());
            ps.setString(12, utenza.getImg());
            ps.setInt(13, utenza.getIdUtente());
            ps.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

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
    public ArrayList<Utenza> searchByCognomeOrUsernameLimited(String nome, int limit) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_UTENZA_BY_SURNAME_OR_USERNAME_LIMIT")) {
            ps.setString(1, "%" + nome + "%");
            ps.setString(2, "%" + nome + "%");
            ps.setInt(3, limit);
            ResultSet rs = ps.executeQuery();

            return getResultAsList(rs);
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
    public ArrayList<Utenza> searchByUsername(String username) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "SEARCH_UTENZA_BY_USERNAME")) {
            ps.setString(1, "%" + username + "%");
            ResultSet rs = ps.executeQuery();

            return getResultAsList(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getNumeroCorsi(int userid) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "GET_COURSE_COUNT_BY_USER_ID")) {
            ps.setInt(1, userid);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    @Override
    public ArrayList<Utenza> findByTipoEmail(String tipo, String email){
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "SEARCH_UTENZA_BY_TIPO_EMAIL")) {
            if (tipo == null) tipo = "";
            if (email == null) email = "";
            ps.setString(1, "%" + tipo + "%");
            ps.setString(2, "%" + email + "%");

            try (ResultSet rs = ps.executeQuery()) {
                return getResultAsList(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Utenza> findByTipo(String tipo) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_UTENZA_BY_TIPO")) {
            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();
            return getResultAsList(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
    public ArrayList<Utenza> findByNazione(String nazione) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_UTENZA_BY_NAZIONE")) {
            ps.setString(1, nazione);
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
    public ArrayList<Utenza> searchByCognomeOrUsername(String pattern) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_UTENZA_BY_SURNAME_OR_USERNAME")) {
            ps.setString(1, "%" + pattern + "%");
            ps.setString(2, "%" + pattern + "%");
            ResultSet rs = ps.executeQuery();

            return getResultAsList(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Utenza extractFromResultSet(ResultSet rs) throws SQLException {

        if (rs.getMetaData().getColumnCount() == 1) {
            return new Utenza(rs.getInt(1), null, null, null, null, null,
                    null, null, null, null, null, null, null);
        } else {
            {
                int idUtente = rs.getInt("IDUtente");
                String nome = rs.getString("Nome");
                String cognome = rs.getString("Cognome");
                Date dataNascita = rs.getDate("DataNascita");
                String indirizzo = rs.getString("Indirizzo");
                String nazione = rs.getString("nazione");
                String telefono = rs.getString("Telefono");
                String email = rs.getString("Email");
                String password = rs.getString("Password");
                Date dataCreazioneAccount = rs.getDate("DataCreazioneAccount");
                String username = rs.getString("Username");
                String tipo = rs.getString("Tipo");
                String img = rs.getString("Immagine");

                return new Utenza(idUtente, nome, cognome, dataNascita, indirizzo, nazione, telefono, email, password, dataCreazioneAccount, username, tipo, img);
            }
        }
    }


    public String getFavouriteCategory(int userID) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "GET_FAVOURITE_CATEGORY")) {
            ps.setInt(1, userID);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getString(1);
            } else {
                return new CategoriaDaoImpl().getMostPurchasedCategory();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Integer> getCountCourseCategory(int userID) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "COUNT_CATEGORY_COURSE")) {
            ps.setInt(1, userID);

            ResultSet rs = ps.executeQuery();
            Map<String, Integer> result = new HashMap<>();
            while (rs.next()) {
                result.put(rs.getString(1), rs.getInt(2 ));
            }
            return  result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

