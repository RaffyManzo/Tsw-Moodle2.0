package model.dao;

import model.beans.Argomento;
import model.beans.Lezione;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ArgomentoDaoImpl extends AbstractDataAccessObject<Argomento> implements ArgomentoDao {

    private static final Logger LOGGER = Logger.getLogger(ArgomentoDaoImpl.class.getName());

    @Override
    public Argomento insertInto(Argomento argomento) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Argomento newArg = null;

        try {
            LOGGER.log(Level.INFO, "Establishing connection...");
            connection = getConnection();
            connection.setAutoCommit(false);

            LOGGER.log(Level.INFO, "Preparing statement...");
            ps = prepareStatement(connection, "INSERT_ARGOMENTO", Statement.RETURN_GENERATED_KEYS);

            LOGGER.log(Level.INFO, "Setting parameters...");
            ps.setDate(1, new java.sql.Date(argomento.getDataCaricamento().getTime()));
            ps.setString(2, argomento.getNome());
            ps.setString(3, argomento.getDescrizione());
            ps.setInt(4, argomento.getLezione());

            LOGGER.log(Level.INFO, "Executing update...");
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.log(Level.INFO, "Fetching generated keys...");
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    newArg = getResultAsObject(rs);
                    LOGGER.log(Level.INFO, "Generated key obtained: " + newArg.getId());
                }


                if(argomento.getFilenames() != null) {
                    if (!argomento.getFilenames().isEmpty()) {
                        LOGGER.log(Level.INFO, "Updating or inserting file {0}", argomento.getFilenames().get(0));
                        PreparedStatement newfile = prepareStatement(connection, "INSERT_FILE");

                        newfile.setString(1, argomento.getFilenames().get(0));
                        newfile.setInt(2, newArg.getId());
                        newfile.executeUpdate();
                    }
                }

                LOGGER.log(Level.INFO, "Committing transaction...");
                connection.commit();
                return newArg;
            } else {
                LOGGER.log(Level.WARNING, "No rows affected, rolling back...");
                connection.rollback();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException occurred, rolling back...", e);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    LOGGER.log(Level.SEVERE, "Error during rollback", rollbackEx);
                    throw new RuntimeException(rollbackEx);
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error closing ResultSet", e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error closing PreparedStatement", e);
                }
            }
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Error closing Connection", e);
                }
            }
        }
        return argomento;
    }



    @Override
    public void update(Argomento argomento) {
        Connection connection = null;
        try {
            connection = getConnection();

            PreparedStatement ps = prepareStatement(connection, "UPDATE_ARGOMENTO");

            connection.setAutoCommit(false);


            ps.setDate(1, new java.sql.Date(argomento.getDataCaricamento().getTime()));
            ps.setString(2, argomento.getNome());
            ps.setString(3, argomento.getDescrizione());
            ps.setInt(4, argomento.getLezione());
            ps.setInt(5, argomento.getId());
            ps.executeUpdate();

            if(!argomento.getFilenames().isEmpty())
                updateOrInsertFile(connection, argomento.getFilenames().get(0), argomento.getId());


            connection.commit();


        } catch (SQLException e) {
            try {
                connection.rollback();
                throw e;
            }catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateOrInsertFile(Connection conn, String newFilename, int idArgomento) {
        if(findFiles(idArgomento).isEmpty()) {
            try {
                PreparedStatement ps = prepareStatement(conn, "INSERT_FILE");

                ps.setString(1, newFilename);
                ps.setInt(2, idArgomento);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else {

            try {
                PreparedStatement ps = prepareStatement(conn, "UPDATE_FILE");

                ps.setString(1, newFilename);
                ps.setInt(2, idArgomento);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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
