package model.dao;

import model.beans.Categoria;
import model.beans.Utenza;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public interface CategoriaDao {
    ArrayList<Categoria> findByNome(String nome) throws SQLException;
    ArrayList<Categoria> findByNomeLimited(String nome, int limit) throws SQLException;
    ArrayList<Categoria> getAllCategorie() throws SQLException;
    int countAllCourseOf(String name) throws SQLException;
    public Map<String, Integer> getCountOfEachCategory(int userID);
    public String getMostPurchasedCategory();
    public Map<Utenza, Integer> getTeacherWithHigherNumberInCategory(String category);
    public int getNumberOfCourseCategory(String category);
    void delete(String nome);

}
