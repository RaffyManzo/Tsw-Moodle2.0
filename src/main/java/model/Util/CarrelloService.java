package model.Util;

import model.beans.Carrello;
import model.dao.CartDao;
import model.dao.CartDaoImpl;

import java.sql.SQLException;

public class CarrelloService {
    private final CartDao carrelloDAO = new CartDaoImpl();

    public void saveCarrello(Carrello carrello) {
        carrelloDAO.saveOrUpdateCarrello(carrello);
    }

    public void deleteItemFromCarrello(int IDCarrello, int IDUtente, int IDCorso) {
        carrelloDAO.deleteFromCarrello(IDCarrello, IDUtente, IDCorso);
    }
}
