package model.Util;

import model.beans.Carrello;
import model.beans.Utenza;
import model.dao.CartDaoImpl;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CarrelloService {
    private final CartDaoImpl carrelloDAO = new CartDaoImpl();
    private static final Logger LOGGER = Logger.getLogger(CarrelloService.class.getName());


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

        int carrelloID = carrelloDAO.getCartIDByUser(user.getIdUtente());


        LOGGER.log(Level.INFO, "Id carrello recuperato: {0}", carrelloID);

        if (carrelloID < 0) {
            carrelloID = carrelloDAO.createCartForUser(user.getIdUtente());
            if(carrelloID < 0)
                return null;
            return new Carrello(user.getIdUtente(), carrelloID);
        } else {
            return carrelloDAO.getCartByUserID(user.getIdUtente());
        }
    }

}
