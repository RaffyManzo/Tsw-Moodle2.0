package model.Util;

import model.beans.Carrello;
import model.beans.Utenza;
import model.dao.CartDao;
import model.dao.CartDaoImpl;

import java.sql.SQLException;

public class CarrelloService {
    private final CartDaoImpl carrelloDAO = new CartDaoImpl();

    public void saveCarrello(Carrello carrello) {
        carrelloDAO.saveOrUpdateCarrello(carrello);
    }

    public void deleteItemFromCarrello(int IDCarrello, int IDUtente, int IDCorso) {
        carrelloDAO.deleteFromCarrello(IDCarrello, IDUtente, IDCorso);
    }



    public Carrello getOrCreateCarrello(Utenza user) {
        if (user == null) {
            return null;
        }

        try {
            int carrelloID = carrelloDAO.getCartIDByUser(user.getIdUtente());
            if (carrelloID < 0) {
                carrelloID = carrelloDAO.createCartForUser(user.getIdUtente());
            }
            return new Carrello(user.getIdUtente(), carrelloID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
