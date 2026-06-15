package com.navios.model;

public class TipoCarga {
    private int idTipoCarga;
    private String nome;
    private String designacao;

    public TipoCarga(int idTipoCarga, String nome, String designacao) {
        this.idTipoCarga = idTipoCarga;
        this.nome        = nome;
        this.designacao  = designacao;
    }

    public int getIdTipoCarga()   { return idTipoCarga; }
    public String getNome()       { return nome; }
    public String getDesignacao() { return designacao; }

    @Override
    public String toString() { return nome; } // usado pelo ComboBox
}