package com.example.snakechild.ciadobusao.util;

/**
 * Created by Pedro on 19/08/2015.
 */
public class EncontroRecorrente {

    private String nome;
    private String hora;
    private String lat;
    private String lng;
    private boolean[] dias;
    private String idUsr;
    private String linha;
    private String referencia;


    public EncontroRecorrente(String idUsr, String nome, String hora,
                              String lat, String lng, boolean[] dias, String linha, String referencia) {
        this.idUsr = idUsr;
        this.nome = nome;
        this.hora = hora;
        this.lat = lat;
        this.lng = lng;
        this.dias = dias;
        this.linha = linha;
        this.referencia = referencia;
    }

    public boolean[] getDias() {
        return dias;
    }

    public String getHora() {
        return hora;
    }

    public String getNome() {
        return nome;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getIdUsr() {
        return idUsr;
    }

    public String getLinha() {
        return linha;
    }

    public String getReferencia() {
        return referencia;
    }
}
