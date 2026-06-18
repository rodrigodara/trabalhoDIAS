package com.navios.model;

public class Navio {
    private int idNavio;
    private String nome;
    private String identificadorIMO;
    private int tipo;
    private int nCompartimentos;
    private int nMaximoCargas;
    private String bandeira;
    private int anoFabrico;
    private String estadoOperacional;

    public Navio(int idNavio, String nome, String identificadorIMO, int tipo,
                 int nCompartimentos, int nMaximoCargas, String bandeira,
                 int anoFabrico, String estadoOperacional) {
        this.idNavio = idNavio;
        this.nome = nome;
        this.identificadorIMO = identificadorIMO;
        this.tipo = tipo;
        this.nCompartimentos = nCompartimentos;
        this.nMaximoCargas = nMaximoCargas;
        this.bandeira = bandeira;
        this.anoFabrico = anoFabrico;
        this.estadoOperacional = estadoOperacional;
    }

    public int getIdNavio() { return idNavio; }
    public String getNome() { return nome; }
    public String getIdentificadorIMO() { return identificadorIMO; }
    public int getTipo() { return tipo; }
    public int getNCompartimentos() { return nCompartimentos; }
    public int getNMaximoCargas() { return nMaximoCargas; }
    public String getBandeira() { return bandeira; }
    public int getAnoFabrico() { return anoFabrico; }
    public String getEstadoOperacional() { return estadoOperacional; }

    public void setIdNavio(int idNavio) { this.idNavio = idNavio; }
    public void setNome(String nome) { this.nome = nome; }
    public void setIdentificadorIMO(String identificadorIMO) { this.identificadorIMO = identificadorIMO; }
    public void setTipo(int tipo) { this.tipo = tipo; }
    public void setNCompartimentos(int nCompartimentos) { this.nCompartimentos = nCompartimentos; }
    public void setNMaximoCargas(int nMaximoCargas) { this.nMaximoCargas = nMaximoCargas; }
    public void setBandeira(String bandeira) { this.bandeira = bandeira; }
    public void setAnoFabrico(int anoFabrico) { this.anoFabrico = anoFabrico; }
    public void setEstadoOperacional(String estadoOperacional) { this.estadoOperacional = estadoOperacional; }
}