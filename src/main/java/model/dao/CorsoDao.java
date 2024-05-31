package model.dao;

import model.beans.Corso;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public interface CorsoDao {
    ArrayList<Corso> findAll() throws SQLException;
    Corso findByID(int id) throws SQLException;
    ArrayList<Corso> findByCategoria(String categoria) throws SQLException;
    ArrayList<Corso> searchByName(String nome) throws SQLException;
    ArrayList<Corso> findByDateRange(Date startDate, Date endDate) throws SQLException;
    ArrayList<Corso> findByCertificazione(String certificazione) throws SQLException;
}

