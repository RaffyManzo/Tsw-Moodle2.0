package model.beans;


public class CreditCard {
    public CreditCard(String numeroCarta, String titolare, String dataScadenza, int idUtente) {
        this.numeroCarta = numeroCarta;
        this.titolare = titolare;
        this.dataScadenza = dataScadenza;
        this.idUtente = idUtente;
    }

    private String numeroCarta;
    private String titolare;
    private String dataScadenza;
    private int idUtente;

    // Getter e setter per NumeroCarta
    public String getNumeroCarta() {
        return numeroCarta;
    }

    public void setNumeroCarta(String numeroCarta) {
        this.numeroCarta = numeroCarta;
    }

    // Getter e setter per Titolare
    public String getTitolare() {
        return titolare;
    }

    public void setTitolare(String titolare) {
        this.titolare = titolare;
    }

    // Getter e setter per DataScadenza
    public String getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(String dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    // Getter e setter per IDUtente
    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }


}
