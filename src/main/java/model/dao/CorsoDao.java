package model.dao;

import model.beans.Corso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface CorsoDao {
    ArrayList<Corso> getAllCourses() throws SQLException;
    Corso findByID(int id) throws SQLException;
    ArrayList<Corso> findByCategoria(String categoria) throws SQLException;
    ArrayList<Corso> searchByName(String nome) throws SQLException;
    ArrayList<Corso> searchByNameLimited(String nome, int limit) throws SQLException;
    ArrayList<Corso> findByCategoriaCorso(String nomeCategoria, String nomeCorso) throws SQLException;

    ArrayList<Corso> findByDateRange(Date startDate, Date endDate) throws SQLException;
    ArrayList<Corso> findByCertificazione(String certificazione) throws SQLException;
    public ArrayList<Corso> findByPrezzo(double prezzo);
    public ArrayList<Corso> findByCreatore(int IDUtente);

    public ArrayList<Corso> findByCategoryPaginated(String categoryId, int page, int itemsPerPage);
    public int countByCategory(String categoryId);
    public ArrayList<Corso> findAllPaginated(int page, int itemsPerPage);
    public int countAll();

}

