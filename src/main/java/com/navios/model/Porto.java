package com.navios.model;
public class Porto {
    private int idPorto;
    private String nome;

    public Porto(int idPorto, String nome) {
        this.idPorto = idPorto;
        this.nome = nome;
    }

    public int getIdPorto() { return idPorto; }
    public String getNome() { return nome; }

    @Override
    public String toString() { return nome; } // usado pelo ComboBox
}