package model.beans;

import java.io.Serializable;

public class Categoria implements Serializable {
    private String nome;

    public Categoria() {
    }

    public Categoria(String nome) {
        this.nome = nome;
    }

    // Getter per nome
    public String getNome() {
        return nome;
    }

    // Setter per nome
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "nome='" + nome + '\'' +
                '}';
    }
}