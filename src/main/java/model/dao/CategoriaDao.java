package model.dao;

import model.beans.Categoria;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CategoriaDao {
    Categoria findByNome(String nome) throws SQLException;
    ArrayList<Categoria> getAllCategorie() throws SQLException;
}
