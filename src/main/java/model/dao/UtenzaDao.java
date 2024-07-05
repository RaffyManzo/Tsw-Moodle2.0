package model.dao;

import model.beans.Utenza;

import java.sql.Date;
import java.util.ArrayList;

public interface UtenzaDao {
    ArrayList<Utenza> getAllUsers();

    Utenza findByID(int id);

    ArrayList<Utenza> findByNome(String nome);


    ArrayList<Utenza> findByCognome(String cognome);

    ArrayList<Utenza> findByDateRange(Date startDate, Date endDate);
    public int getNumeroCorsi(int userid);
    Utenza findByEmail(String email);

    Utenza findByUsername(String username);

    ArrayList<Utenza> findByTipo(String tipo);

    ArrayList<Utenza> findByTelefono(String telefono);

    ArrayList<Utenza> findByCitta(String citta);

    ArrayList<Utenza> searchByCognomeOrUsername(String tipo);
    ArrayList<Utenza> searchByCognomeOrUsernameLimited(String nome, int limit);
    ArrayList<Utenza> searchByUsername(String username);
    ArrayList<Utenza> findByTipoEmail(String tipo, String email);
}
