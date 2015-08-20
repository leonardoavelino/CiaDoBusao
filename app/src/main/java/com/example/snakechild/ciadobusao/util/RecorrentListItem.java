package com.example.snakechild.ciadobusao.util;

/**
 * Created by Pedro on 19/08/2015.
 */
public class RecorrentListItem {
    private String diasSemana;
    private String nome;
    private String hora;

    public RecorrentListItem(String nome, String hora, String diasSemana) {
        this.nome = nome;
        this.diasSemana = diasSemana;
        this.hora = hora;
    }

    public String getNome() {
        return nome;
    }

    public String getDiasSemana() {
        return diasSemana;
    }

    public String getHora() {
        return hora;
    }
}
