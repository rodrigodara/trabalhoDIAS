package com.navios.model;

import java.time.LocalDate;

public class Viagem {

    private int idViagem;
    private int idNavio;
    private LocalDate dataPartida;
    private LocalDate dataChegadaPrevista;
    private int portoOrigem;
    private int portoDestino;
    private String estadoViagem;

    public Viagem(int idViagem, int idNavio, LocalDate dataPartida,
                  LocalDate dataChegadaPrevista, int portoOrigem,
                  int portoDestino, String estadoViagem) {
        this.idViagem            = idViagem;
        this.idNavio             = idNavio;
        this.dataPartida         = dataPartida;
        this.dataChegadaPrevista = dataChegadaPrevista;
        this.portoOrigem         = portoOrigem;
        this.portoDestino        = portoDestino;
        this.estadoViagem        = estadoViagem;
    }

    // Getters
    public int       getIdViagem()            { return idViagem; }
    public int       getIdNavio()             { return idNavio; }
    public LocalDate getDataPartida()         { return dataPartida; }
    public LocalDate getDataChegadaPrevista() { return dataChegadaPrevista; }
    public int       getPortoOrigem()         { return portoOrigem; }
    public int       getPortoDestino()        { return portoDestino; }
    public String    getEstadoViagem()        { return estadoViagem; }
}