package model.beans;

import java.sql.Date;

public class Corso {
    private final int idCorso;
    private final String nomeCategoria;
    private final String nome;
    private final String descrizione;
    private final String immagine;
    private final String certificazione;
    private final Date dataCreazione;



    // Costruttore con argomenti
    public Corso(int idCorso, String nomeCategoria, String nome, String descrizione, String immagine, String certificazione, Date dataCreazione) {
        this.idCorso = idCorso;
        this.nomeCategoria = nomeCategoria;
        this.nome = nome;
        this.descrizione = descrizione;
        this.immagine = immagine;
        this.certificazione = certificazione;
        this.dataCreazione = dataCreazione;
    }

    // Getter
    public int getIdCorso() {
        return idCorso;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getImmagine() {
        return immagine;
    }

    public String getCertificazione() {
        return certificazione;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    @Override
    public String toString() {
        return "Corso{" +
                "idCorso=" + idCorso +
                ", nomeCategoria='" + nomeCategoria + '\'' +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", immagine='" + immagine + '\'' +
                ", certificazione='" + certificazione + '\'' +
                ", dataCreazione=" + dataCreazione +
                '}';
    }
}

