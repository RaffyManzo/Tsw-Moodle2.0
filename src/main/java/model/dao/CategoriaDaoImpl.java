package model.dao;

import model.beans.Categoria;
import java.sql.*;
import java.util.ArrayList;

public class CategoriaDaoImpl extends AbstractDataAccessObject<Categoria> implements CategoriaDao {
    ArrayList<Categoria> categorie = new ArrayList<>();

    public ArrayList<Categoria> getCategorie() {
        if(categorie.isEmpty())
            return getAllCategorie();
        else
            return categorie;
    }

    @Override
    public ArrayList<Categoria> findByNome(String nome){
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
    public ArrayList<Categoria> getAllCategorie(){
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
    public int countAllCourseOf(String name){
        //
        return -1;
    }

    @Override
    protected Categoria extractFromResultSet(ResultSet rs) throws SQLException {
        String nomeCategoria = rs.getString("Nome");
        return new Categoria(nomeCategoria);
    }

    @Override
    protected boolean insertInto(Categoria categoria){
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "INSERT_CATEGORIA")) {
            ps.setString(1, categoria.getNome());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    protected void update(Categoria categoria) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection,"UPDATE_CATEGORIA")) {
            ps.setString(1, categoria.getNome());
           ps.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    public void delete(String nome) {
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "DELETE_CATEGORIA")) {
            ps.setString(1, nome);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
