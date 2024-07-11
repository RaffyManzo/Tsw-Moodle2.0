package model.dao;

import model.beans.Corso;
import model.beans.Utenza;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface CorsoDao {
    ArrayList<Corso> getAllCourses() throws SQLException;
    Corso findByID(int id) throws SQLException;
    ArrayList<Corso> findByCategoria(String categoria) throws SQLException;
    ArrayList<Corso> searchByName(String nome) throws SQLException;
    ArrayList<Corso> searchByNameLimited(String nome, int limit) throws SQLException;
    ArrayList<Corso> filteredSearchCourse(String nomeCategoria, String nomeCorso) throws SQLException;

    ArrayList<Corso> findByDateRange(Date startDate, Date endDate) throws SQLException;
    ArrayList<Corso> findByCertificazione(String certificazione) throws SQLException;
    public ArrayList<Corso> findByPrezzo(double prezzo);
    public ArrayList<Utenza> getUtentiAcquistati(int id);
    public ArrayList<Corso> getCorsiAcquistati(int id);
    public ArrayList<Corso> findByCreatore(int IDUtente);

    public ArrayList<Corso> findByCategoryPaginated(String categoryId, int page, int itemsPerPage);
    public int countByCategory(String categoryId);
    public ArrayList<Corso> findAllPaginated(int page, int itemsPerPage);
    public int countAll();
    public ArrayList<Corso> searchCreatorCoursesByName(String nome, int creator);
    public ArrayList<Corso> getAllCoursesAdmin();
    ArrayList<Corso> getCorsiOrdine(int id);

}

