package model.dao;

import model.beans.Corso;
import model.beans.Utenza;

import java.sql.*;
import java.util.ArrayList;
import java.util.Set;

public class CorsoDaoImpl extends AbstractDataAccessObject<Corso> implements CorsoDao{

    ArrayList<Corso> courses = new ArrayList<>();

    public ArrayList<Corso> getCourses() {
        if(courses.isEmpty())
            return getAllCourses();
        else
            return courses;
    }

    @Override
    public Corso insertInto(Corso corso)  {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "INSERT_CORSO", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, corso.getNomeCategoria());
            ps.setString(2, corso.getNome());
            ps.setString(3, corso.getDescrizione());
            ps.setString(4, corso.getImmagine());
            ps.setString(5, corso.getCertificazione());
            ps.setDate(6, new java.sql.Date(corso.getDataCreazione().getTime()));
            ps.setDouble(8, corso.getPrezzo());
            ps.setInt(9, corso.getCreatore().getIdUtente());

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
        return corso;
    }

    @Override
    public void update(Corso corso)  {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection,"UPDATE_CORSO")) {
            ps.setString(1, corso.getNomeCategoria());
            ps.setString(2, corso.getNome());
            ps.setString(3, corso.getDescrizione());
            ps.setString(4, corso.getImmagine());
            ps.setString(5, corso.getCertificazione());
            ps.setDate(6, new java.sql.Date(corso.getDataCreazione().getTime()));
            ps.setInt(9, corso.getIdCorso());
            ps.setDouble(7, corso.getPrezzo());
            ps.setInt(8, corso.getCreatore().getIdUtente());
            ps.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

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
    public ArrayList<Corso> getCorsiOrdine(int id)  {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "DETTAGLI_ORDINE");){
             ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                courses.addAll(getResultAsList(rs));
                return courses;
            }} catch (SQLException e) {
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
    public ArrayList<Corso> searchByNameLimited(String nome, int limit){
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "SEARCH_CORSI_BY_NOME_LIMIT")) {
            ps.setString(1, "%" + nome + "%");
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                return getResultAsList(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Corso> findByCategoriaCorso(String nomeCategoria, String nomeCorso){
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "SEARCH_CORSO_BY_CATEGORIA_NOME")) {
            ps.setString(1, "%" + nomeCategoria + "%");
            ps.setString(2, "%" + nomeCorso + "%");

            try (ResultSet rs = ps.executeQuery()) {
                return getResultAsList(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Corso> getCourseOfOrder(int idOrder) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "GET_COURSES_OF_ORDER")) {

            ps.setInt(1, idOrder);


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
    public ArrayList<Corso> findByPrezzo(double prezzo)  {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_CORSI_BY_PREZZO")) {
            ps.setDouble(1, prezzo);
            try (ResultSet rs = ps.executeQuery()) {
                return getResultAsList(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Corso> findByCreatore(int IDUtente)  {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_CORSI_BY_CREATORE")) {
            ps.setDouble(1, IDUtente);
            try (ResultSet rs = ps.executeQuery()) {
                return getResultAsList(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Corso> findByCategoryPaginated(String categoryId, int page, int itemsPerPage) {
        int offset = (page - 1) * itemsPerPage;

        try (Connection conn = getConnection();
             PreparedStatement ps = prepareStatement(conn, "SELECT_CORSI_BY_CATEGORY_PAGINATED")) {

            ps.setString(1, categoryId);
            ps.setInt(2, itemsPerPage);
            ps.setInt(3, offset);
             ResultSet rs = ps.executeQuery();

            return getResultAsList(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int countByCategory(String categoryId) {
        try (Connection conn = getConnection();
             PreparedStatement ps = prepareStatement(conn, "COUNT_CORSI_BY_CATEGORY")) {
            ps.setString(1, categoryId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public ArrayList<Corso> findAllPaginated(int page, int itemsPerPage) {
        int offset = (page - 1) * itemsPerPage;

        try (Connection conn = getConnection();
             PreparedStatement ps = prepareStatement(conn, "SELECT_ALL_CORSI_PAGINATED")) {

            ps.setInt(1, itemsPerPage);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();

            return getResultAsList(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int countAll() {
        try (Connection conn = getConnection();
             PreparedStatement stmt = prepareStatement(conn, "COUNT_ALL_CORSI");
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
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
        Utenza creatore = new UtenzaDaoImpl().findByID(rs.getInt("creatore"));
        Double prezzo = rs.getDouble("prezzo");
        int numeroAcquisti = rs.getInt("numeroAcquisti");

        return new Corso(idCorso, nomeCategoria, nome, descrizione,
                immagine, certificazione, dataCreazione, creatore, prezzo, numeroAcquisti);
    }
}
