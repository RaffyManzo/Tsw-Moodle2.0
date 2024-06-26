package model.beans;

import org.json.simple.JSONObject;

import java.util.Date;

public class Utenza {
    private final int idUtente;
    private final String nome;
    private final String cognome;
    private final Date dataNascita;
    private final String indirizzo;
    private final String citta;
    private String telefono;
    private final String email;
    private final String password;
    private final Date dataCreazioneAccount;
    private final String username;
    private final String tipo;

    private String img;


    public Utenza(int idUtente, String nome, String cognome, Date dataNascita, String indirizzo, String citta, String telefono, String email, String password, Date dataCreazioneAccount, String username, String tipo, String img) {
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
        this.img = img;
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

    public String getTelefono() {
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

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("nome", nome);
        json.put("cognome", cognome);
        json.put("email", email);
        json.put("tipo", tipo);
        json.put("img", img);
        return json;
    }

    public String getImg() {
        return img;
    }

    public void setImage(String fileName) {
        this.img = fileName;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
