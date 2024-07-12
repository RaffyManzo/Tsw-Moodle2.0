package model.beans;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Argomento {
    private int id;
    private Timestamp dataCaricamento;
    private String nome;
    private String descrizione;
    private int lezione;

    public ArrayList<String> getFilenames() {
        return filenames;
    }

    public void setFilenames(ArrayList<String> filenames) {
        this.filenames = filenames;
    }

    private ArrayList<String> filenames;

    // Costruttore
    public Argomento(int id, Timestamp dataCaricamento, String nome, String descrizione, int lezione) {
        this.id = id;
        this.dataCaricamento = dataCaricamento;
        this.nome = nome;
        this.descrizione = descrizione;
        this.lezione = lezione;
    }

    public Argomento() {

    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDataCaricamento() {
        return dataCaricamento;
    }

    public void setDataCaricamento(Timestamp dataCaricamento) {
        this.dataCaricamento = dataCaricamento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getLezione() {
        return lezione;
    }

    public void setLezione(int lezione) {
        this.lezione = lezione;
    }

    public void setIdLezione(int lessonId) {
        this.lezione = lessonId;
    }
}
