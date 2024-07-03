package model.beans;

import java.sql.Date;
import java.util.Objects;

public class Corso {
    private final int idCorso;
    private final String nomeCategoria;
    private final String nome;
    private final String descrizione;
    private final String immagine;
    private final String certificazione;
    private final Date dataCreazione;

    private Utenza creatore;


    private final double prezzo;

    public int getNumeroAcquisti() {
        return numeroAcquisti;
    }

    private int numeroAcquisti;


    // Costruttore con argomenti
    public Corso(int idCorso, String nomeCategoria, String nome, String descrizione, String immagine, String certificazione, Date dataCreazione, Utenza creatore, double prezzo,int numeroAcquisti) {
        this.idCorso = idCorso;
        this.nomeCategoria = nomeCategoria;
        this.nome = nome;
        this.descrizione = descrizione;
        this.immagine = immagine;
        this.certificazione = certificazione;
        this.dataCreazione = dataCreazione;
        this.creatore = creatore;
        this.prezzo = prezzo;
        this.numeroAcquisti = numeroAcquisti;
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
    public double getPrezzo() {
        return prezzo;
    }

    public Utenza getCreatore() {
        return creatore;
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
                ", prezzo='" + prezzo + '\'' +
                ", creatore='" + creatore + '\'' +
                ", dataCreazione=" + dataCreazione +
                '}';
    }

    // equals e hashCode (necessario per l'uso come chiave di Map)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Corso corso = (Corso) o;
        return idCorso == corso.idCorso;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCorso);
    }
}

