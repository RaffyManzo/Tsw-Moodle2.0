package model.beans;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Carrello {


    private final Map<Corso, Integer> cart = new ConcurrentHashMap<>();


    public void setIDUtente(int IDUtente) {
        this.IDUtente = IDUtente;
    }

    public void setIDCarrello(int IDCarrello) {
        this.IDCarrello = IDCarrello;
    }

    private  int IDUtente;
    private  int IDCarrello;

    public Carrello(Map<Corso, Integer> sessionCart, int idUtente, int idCarrello) {
        IDUtente = idUtente;
        IDCarrello = idCarrello;
        if (sessionCart != null)
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

    public void setCart (Map<Corso, Integer> cart) {
        this.cart.putAll(cart);
    }

    public void replace (Map<Corso, Integer> cart) {
        this.cart.clear();
        this.cart.putAll(cart);
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

    public void addCorso(Corso corso, int quantita) {
        cart.put(corso, cart.getOrDefault(corso, 0) + quantita);
    }

    public void removeCorso(Corso corso) {
        cart.remove(corso);
    }

    public void decreaseQuantita(Corso corso, int quantita) {
        if (cart.containsKey(corso)) {
            int currentQuantita = cart.get(corso);
            if (currentQuantita <= quantita) {
                cart.remove(corso);
            } else {
                cart.put(corso, currentQuantita - quantita);
            }
        }
    }
}
