package model.dao;

import model.beans.Argomento;
import model.beans.Lezione;

import java.sql.*;
import java.util.ArrayList;


public class ArgomentoDaoImpl extends AbstractDataAccessObject<Argomento> implements ArgomentoDao {
    @Override
    public Argomento insertInto(Argomento argomento) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "INSERT_ARGOMENTO", Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, new java.sql.Date(argomento.getDataCaricamento().getTime()));
            ps.setString(2, argomento.getNome());
            ps.setString(3, argomento.getDescrizione());
            ps.setInt(4, argomento.getLezione());

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
            ps.setInt(4, argomento.getLezione());
            ps.setInt(5, argomento.getId());
            ps.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void updateOrInsertFile(String newFilename, int idArgomento) {
        if(findFiles(idArgomento).isEmpty()) {
            insertFile(newFilename, idArgomento);
        } else {
            updateFile(newFilename, idArgomento);
        }
    }

    private void updateFile(String newFilename, int idArgomento) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "UPDATE_FILE")) {

            ps.setString(1, newFilename);
            ps.setInt(2, idArgomento);
            ps.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }


    private void insertFile(String newFilename, int idArgomento) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "INSERT_FILE")) {

            ps.setString(1, newFilename);
            ps.setInt(2, idArgomento);
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
                    return getResultAsObject(rs);
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return null;
    }

    @Override
    public ArrayList<String> findFiles(int id) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_ALL_FILES")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                ArrayList<String> fileNames = new ArrayList<>();
                while (rs.next()) {
                    fileNames.add(rs.getString("filename"));
                }

                return fileNames;
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
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

        if (rs.getMetaData().getColumnCount() == 1) {
            return new Argomento(rs.getInt(1), null, null, null, 0);
        } else {
            int id = rs.getInt("IDArgomento");
            Timestamp dataCaricamento = rs.getTimestamp("dataCaricamento");
            String nome = rs.getString("nome");
            String descrizione = rs.getString("descrizione");
            int idLezione = rs.getInt("idLezione");
            Argomento arg = new Argomento(id, dataCaricamento, nome, descrizione, idLezione);

            arg.setFilenames(findFiles(id));

            return arg;
        }
    }
}
