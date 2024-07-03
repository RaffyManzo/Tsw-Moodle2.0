package model.dao;

import model.beans.CreditCard;

public interface CreditCardDao {
    public CreditCard findByUserID(int id);
    public void saveOrUpdateCard(CreditCard card);

    }
