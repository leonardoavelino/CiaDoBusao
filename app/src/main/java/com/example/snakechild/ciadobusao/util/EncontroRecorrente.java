package com.example.snakechild.ciadobusao.util;

/**
 * Created by Pedro on 19/08/2015.
 */
public class EncontroRecorrente {

    String nome;
    String hora;
    boolean[] dias;

    public EncontroRecorrente(String nome, String hora, boolean[] dias) {
        this.nome = nome;
        this.dias = dias;
        this.hora = hora;
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
}
