package model.beans;

import java.util.Date;

public class Argomento {
    private int id;
    private Date dataCaricamento;
    private String nome;
    private String descrizione;
    private int lezione;

    // Costruttore
    public Argomento(int id, Date dataCaricamento, String nome, String descrizione, int lezione) {
        this.id = id;
        this.dataCaricamento = dataCaricamento;
        this.nome = nome;
        this.descrizione = descrizione;
        this.lezione = lezione;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataCaricamento() {
        return dataCaricamento;
    }

    public void setDataCaricamento(Date dataCaricamento) {
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
}
