package model.dao;

import model.beans.Categoria;
import model.beans.Utenza;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoriaDaoImpl extends AbstractDataAccessObject<Categoria> implements CategoriaDao {
    ArrayList<Categoria> categorie = new ArrayList<>();

    public ArrayList<Categoria> getCategorie() {
        if (categorie.isEmpty())
            return getAllCategorie();
        else
            return categorie;
    }

    @Override
    public ArrayList<Categoria> findByNome(String nome) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "SEARCH_CATEGORIA_BY_NOME")) {
            ps.setString(1, "%" + nome + "%");

            try (ResultSet rs = ps.executeQuery()) {
                return getResultAsList(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Categoria> findByNomeLimited(String nome, int limit) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "SEARCH_CATEGORIA_BY_NOME_LIMIT")) {
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
    public ArrayList<Categoria> getAllCategorie() {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "FIND_ALL_CATEGORIE");
             ResultSet rs = ps.executeQuery()) {
            categorie.addAll(getResultAsList(rs));
            return categorie;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countAllCourseOf(String name) {
        //
        return -1;
    }

    @Override
    protected Categoria extractFromResultSet(ResultSet rs) throws SQLException {
        String nomeCategoria = rs.getString("Nome");
        return new Categoria(nomeCategoria);
    }

    @Override
    protected Categoria insertInto(Categoria categoria) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "INSERT_CATEGORIA", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, categoria.getNome());

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
        return categoria;
    }

    @Override
    protected void update(Categoria categoria) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "UPDATE_CATEGORIA")) {
            ps.setString(1, categoria.getNome());
            ps.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void changeName(String categoria, String nome) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "UPDATE_CATEGORIA")) {
            ps.setString(1, nome);
            ps.setString(2, categoria);
            ps.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void delete(String nome) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "DELETE_CATEGORIA")) {
            ps.setString(1, nome);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Integer> getCountOfEachCategory(int userID) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "COUNT_CATEGORY_COURSE");
             ResultSet rs = ps.executeQuery()) {
            Map<String, Integer> result = new HashMap<>();

            while (rs.next()) {
                result.put(rs.getString(1), rs.getInt(2));
            }

            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<Utenza, Integer> getTeacherWithHigherNumberInCategory(String category) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "GET_CREATORS_WITH_HIGHER_NUMBER_OF_COURSE")) {

            ps.setString(1, category);

            ResultSet rs = ps.executeQuery();
            Map<Utenza, Integer> result = new HashMap<>();

            while (rs.next()) {
                Utenza utenza = new UtenzaDaoImpl().extractFromResultSet(rs);
                result.put(utenza, rs.getInt(1));
            }

            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getMostPurchasedCategory() {
        String mostPurchasedCategory = null;
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "CATEGORY_MOST_PURCHASED")) {


            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                mostPurchasedCategory = rs.getString(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return mostPurchasedCategory;
    }

    @Override
    public int getNumberOfCourseCategory(String category) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "GET_COUNT_OF_CATEGORY")) {
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }
}
