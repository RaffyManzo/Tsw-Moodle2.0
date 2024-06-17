package model.beans;

import java.sql.Date;

public class Utenza {
    private final int idUtente;
    private final String nome;
    private final String cognome;
    private final Date dataNascita;
    private final String indirizzo;
    private final String citta;
    private final int telefono;
    private final String email;
    private final String password;
    private final Date dataCreazioneAccount;
    private final String username;
    private final String tipo;

    public Utenza(int idUtente, String nome, String cognome, Date dataNascita, String indirizzo, String citta, int telefono, String email, String password, Date dataCreazioneAccount, String username, String tipo) {
        this.idUtente = idUtente;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.indirizzo = indirizzo;
        this.citta = citta;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.dataCreazioneAccount = dataCreazioneAccount;
        this.username = username;
        this.tipo = tipo;
    }

    // Getters
    public int getIdUtente() {
        return idUtente;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getCitta() {
        return citta;
    }

    public int getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Date getDataCreazioneAccount() {
        return dataCreazioneAccount;
    }

    public String getUsername() {
        return username;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "Utenza{" +
                "idUtente=" + idUtente +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", dataNascita=" + dataNascita +
                ", indirizzo='" + indirizzo + '\'' +
                ", citta='" + citta + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", dataCreazioneAccount=" + dataCreazioneAccount +
                ", username='" + username + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
