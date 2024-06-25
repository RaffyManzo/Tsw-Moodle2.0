package model.beans;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Carrello {


    private final Map<Corso, Integer> cart = new ConcurrentHashMap<>();



    private final int IDUtente;
    private final int IDCarrello;

    public Carrello(Map<Corso, Integer> sessionCart, int idUtente, int idCarrello) {
        IDUtente = idUtente;
        IDCarrello = idCarrello;
        cart.putAll(sessionCart);
    }

    public Carrello(int IDUtente, int IDCarrello) {
        this.IDUtente = IDUtente;
        this.IDCarrello = IDCarrello;
    }

    public Carrello(ArrayList<Corso> courses, ArrayList<Integer> quantity, int idUtente, int idCarrello) {
        IDUtente = idUtente;
        IDCarrello = idCarrello;
        if (courses.size() != quantity.size())
            throw new IllegalArgumentException("Arrays must have same size");
        else {
            int index = 0;
            for (Corso c: courses) {
                cart.put(c, quantity.get(index));
                index++;
            }
        }
    }

    public Map<Corso, Integer> getCart() {
        return cart;
    }


    public Corso getElementAtIndex(int index) {
        List<Corso> keys = new ArrayList<>(cart.keySet());
        if (index < 0 || index >= keys.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + keys.size());
        }
        return keys.get(index);
    }

    public int getIDUtente() {
        return IDUtente;
    }

    public int getIDCarrello() {
        return IDCarrello;
    }
}
