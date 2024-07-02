package model.dao;

import model.beans.Categoria;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CategoriaDao {
    ArrayList<Categoria> findByNome(String nome) throws SQLException;
    ArrayList<Categoria> getAllCategorie() throws SQLException;
    int countAllCourseOf(String name) throws SQLException;
}
