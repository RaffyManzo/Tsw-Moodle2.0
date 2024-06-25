package model.dao;

import model.beans.Carrello;

public interface CartDao {
    void deleteFromCarrello(int IDCorso, int IDCarrello, int IDUtente);

    void saveOrUpdateCarrello(Carrello carrello);

}
