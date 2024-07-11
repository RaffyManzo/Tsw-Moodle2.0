package model.dao;

import model.beans.Carrello;
import model.beans.Corso;
import model.beans.CreditCard;
import model.beans.Ordine;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrdineDao {
    public void purchaseCoursesFromCart(Carrello carrello, CreditCard card) throws SQLException;
    void delete(int id);
    ArrayList<Ordine> getAllOrdini() throws SQLException;
    ArrayList<Ordine> findByUtenteId(int idUtente) throws SQLException;
}
