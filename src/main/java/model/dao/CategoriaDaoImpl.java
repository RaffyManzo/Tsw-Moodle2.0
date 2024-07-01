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
    public Categoria findByNome(String nome){
        try (Connection connection = getConnection();
             PreparedStatement ps = prepareStatement(connection, "SEARCH_CATEGORIA_BY_NOME")) {
            ps.setString(1, nome);
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

    @Override
    protected void delete(int id) {
        //?????
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
