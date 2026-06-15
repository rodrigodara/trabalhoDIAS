package com.navios.model;

import java.time.LocalDate;

public class Tripulante {
    private int idTripulante;
    private String nome;
    private String sobrenome;
    private String estadoDisponibilidade;
    private String nacionalidade;
    private LocalDate dataNascimento;

    public Tripulante(int idTripulante, String nome, String sobrenome,
                      String estadoDisponibilidade, String nacionalidade,
                      LocalDate dataNascimento) {
        this.idTripulante          = idTripulante;
        this.nome                  = nome;
        this.sobrenome             = sobrenome;
        this.estadoDisponibilidade = estadoDisponibilidade;
        this.nacionalidade         = nacionalidade;
        this.dataNascimento        = dataNascimento;
    }

    public int getIdTripulante()             { return idTripulante; }
    public String getNome()                  { return nome; }
    public String getSobrenome()             { return sobrenome; }
    public String getEstadoDisponibilidade() { return estadoDisponibilidade; }
    public String getNacionalidade()         { return nacionalidade; }
    public LocalDate getDataNascimento()     { return dataNascimento; }

    @Override
    public String toString() { return nome + " " + sobrenome; }
}