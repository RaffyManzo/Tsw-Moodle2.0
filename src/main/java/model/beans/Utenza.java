package model.beans;

import org.json.simple.JSONObject;

import java.util.Date;

public class Utenza {
    private  int idUtente;
    private  String nome;
    private  String cognome;
    private  Date dataNascita;
    private  String indirizzo;
    private  String citta;
    private String telefono;
    private  String email;

    private  String password;
    private  Date dataCreazioneAccount;
    private  String username;

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDataCreazioneAccount(Date dataCreazioneAccount) {
        this.dataCreazioneAccount = dataCreazioneAccount;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    private  String tipo;

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
