package com.navios.model;

public class Carga {
    private int idCarga;
    private String designacao;
    private int tipo;
    private float volume;
    private float peso;
    private int portoCarga;
    private int portoDescarga;

    public Carga(int idCarga, String designacao, int tipo, float volume, float peso, int portoCarga, int portoDescarga) {
        this.idCarga = idCarga;
        this.designacao = designacao;
        this.tipo = tipo;
        this.volume = volume;
        this.peso = peso;
        this.portoCarga = portoCarga;
        this.portoDescarga = portoDescarga;
    }

    public int getIdCarga()        { return idCarga; }
    public String getDesignacao()  { return designacao; }
    public int getTipo()           { return tipo; }
    public float getVolume()       { return volume; }
    public float getPeso()         { return peso; }
    public int getPortoCarga()     { return portoCarga; }
    public int getPortoDescarga()  { return portoDescarga; }
}