package model.beans;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class Ordine {
    private int id;
    private String numeroCarta;
    private  int Idutente;
    private Date dataPagamento;

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    private Timestamp timestamp;
    private UUID uuid;

    public Set<Corso> getCorsiAcquistati() {
        return corsiAcquistati;
    }

    public void setCorsiAcquistati(Set<Corso> corsiAcquistati) {
        this.corsiAcquistati = corsiAcquistati;
    }

    private Set<Corso> corsiAcquistati;

    public Ordine(int id, String numeroCarta, int idutente, Date dataPagamento, double totale) {
        this.id = id;
        this.numeroCarta = numeroCarta;
        this.Idutente = idutente;
        this.dataPagamento = dataPagamento;
        this.totale = totale;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.uuid = UUID.randomUUID();
    }

    // ... altri metodi ...

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Ordine(int id, String numeroCarta, int idutente, Date dataPagamento, double totale, Timestamp tmp, UUID uuid) {
        this.id = id;
        this.numeroCarta = numeroCarta;
        this.Idutente = idutente;
        this.dataPagamento = dataPagamento;
        this.totale = totale;
        this.timestamp = tmp;
        this.uuid = uuid;
    }

    public Ordine(String numeroCarta, int idutente, Date dataPagamento, double totale) {
        this.numeroCarta = numeroCarta;
        this.Idutente = idutente;
        this.dataPagamento = dataPagamento;
        this.totale = totale;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.uuid = UUID.randomUUID();
    }

    private double totale;

    public int getId() {
        return id;
    }


    public String getNumeroCarta() {
        return numeroCarta;
    }

    public int getIdutente() {
        return Idutente;
    }


    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public double getTotale() {
        return totale;
    }

}
