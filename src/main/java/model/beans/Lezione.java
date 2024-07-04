package model.beans;

import java.util.ArrayList;

public class Lezione {
    private int id;
    private String descrizione;
    private String titolo;
    private int idCorso;
    private ArrayList<Argomento> argomenti;

    // Costruttore
    public Lezione(int id, String descrizione, String titolo, int idCorso, ArrayList<Argomento> argomenti) {
        this.id = id;
        this.descrizione = descrizione;
        this.titolo = titolo;
        this.idCorso = idCorso;
        this.argomenti = argomenti;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public int getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(int idCorso) {
        this.idCorso = idCorso;
    }

    public ArrayList<Argomento> getArgomenti() {
        return argomenti;
    }

    public void setArgomenti(ArrayList<Argomento> argomenti) {
        this.argomenti = argomenti;
    }
}
