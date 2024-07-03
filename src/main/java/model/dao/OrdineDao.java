package model.dao;

import model.beans.Carrello;
import model.beans.CreditCard;

import java.sql.SQLException;

public interface OrdineDao {
    public void purchaseCoursesFromCart(Carrello carrello, CreditCard card) throws SQLException;
}
