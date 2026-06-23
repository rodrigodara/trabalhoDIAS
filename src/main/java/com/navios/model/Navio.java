package com.navios.model;

public class Navio {
    private int    idNavio;
    private String nome;
    private String identificadorIMO;
    private int    tipo;
    private int    nCompartimentos;
    private int    nMaximoCargas;
    private double capacidadeCarga;   // ← NOVO (toneladas)
    private String bandeira;
    private int    anoFabrico;
    private String estadoOperacional;

    public Navio(int idNavio, String nome, String identificadorIMO, int tipo,
                 int nCompartimentos, int nMaximoCargas, double capacidadeCarga,
                 String bandeira, int anoFabrico, String estadoOperacional) {
        this.idNavio           = idNavio;
        this.nome              = nome;
        this.identificadorIMO  = identificadorIMO;
        this.tipo              = tipo;
        this.nCompartimentos   = nCompartimentos;
        this.nMaximoCargas     = nMaximoCargas;
        this.capacidadeCarga   = capacidadeCarga;
        this.bandeira          = bandeira;
        this.anoFabrico        = anoFabrico;
        this.estadoOperacional = estadoOperacional;
    }

    public int    getIdNavio()            { return idNavio; }
    public String getNome()               { return nome; }
    public String getIdentificadorIMO()   { return identificadorIMO; }
    public int    getTipo()               { return tipo; }
    public int    getNCompartimentos()    { return nCompartimentos; }
    public int    getNMaximoCargas()      { return nMaximoCargas; }
    public double getCapacidadeCarga()    { return capacidadeCarga; }   // ← NOVO
    public String getBandeira()           { return bandeira; }
    public int    getAnoFabrico()         { return anoFabrico; }
    public String getEstadoOperacional()  { return estadoOperacional; }

    public void setIdNavio(int idNavio)                     { this.idNavio = idNavio; }
    public void setNome(String nome)                         { this.nome = nome; }
    public void setIdentificadorIMO(String id)               { this.identificadorIMO = id; }
    public void setTipo(int tipo)                            { this.tipo = tipo; }
    public void setNCompartimentos(int n)                    { this.nCompartimentos = n; }
    public void setNMaximoCargas(int n)                      { this.nMaximoCargas = n; }
    public void setCapacidadeCarga(double capacidadeCarga)   { this.capacidadeCarga = capacidadeCarga; } // ← NOVO
    public void setBandeira(String bandeira)                 { this.bandeira = bandeira; }
    public void setAnoFabrico(int anoFabrico)                { this.anoFabrico = anoFabrico; }
    public void setEstadoOperacional(String estado)          { this.estadoOperacional = estado; }
}