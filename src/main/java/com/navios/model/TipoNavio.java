package com.navios.model;

public class TipoNavio {
    private final int idTipoNavio;
    private final String nome;
    private final int nMaximoCargas;

    public TipoNavio(int idTipoNavio, String nome, int nMaximoCargas) {
        this.idTipoNavio = idTipoNavio;
        this.nome = nome;
        this.nMaximoCargas = nMaximoCargas;
    }

    public int getIdTipoNavio() { return idTipoNavio; }
    public String getNome() { return nome; }
    public int getNMaximoCargas() { return nMaximoCargas; }

    @Override
    public String toString() {
        return nome;
    }
}
