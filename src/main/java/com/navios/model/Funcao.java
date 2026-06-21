package com.navios.model;

public class Funcao {
    private final int idFuncao;
    private final String nome;

    public Funcao(int idFuncao, String nome) {
        this.idFuncao = idFuncao;
        this.nome = nome;
    }

    public int getIdFuncao() { return idFuncao; }
    public String getNome() { return nome; }

    @Override
    public String toString() {
        return nome;
    }
}
