package com.example.snakechild.ciadobusao.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pedro on 19/08/2015.
 */
public class ControladorEncontroRecorrente {

    private static ControladorEncontroRecorrente instance;
    List<EncontroRecorrente> encontroRecorrenteList;
    private DataSource dataSource;

    private ControladorEncontroRecorrente(Context context) {
        dataSource = new DataSource(context);
        dataSource.open();
        encontroRecorrenteList = dataSource.getTodosEncontros();
    }

    public static ControladorEncontroRecorrente getInstance(Context context) {
        if (instance == null) {
            instance = new ControladorEncontroRecorrente(context);
        }
        return instance;

    }

    public void addEncontro(EncontroRecorrente encontroRecorrente) {
        encontroRecorrenteList.add(encontroRecorrente);
        dataSource.insertEncontro(encontroRecorrente);
    }

    public List<EncontroRecorrente> getEncontroRecorrenteList() {
        return encontroRecorrenteList;
    }

    public void removeEncontro(String nome) {
        for (EncontroRecorrente encontroRecorrente : encontroRecorrenteList) {
            if (encontroRecorrente.getNome().equals(nome)) {
                dataSource.removeEncontro(encontroRecorrente);
                encontroRecorrenteList.remove(encontroRecorrente);
                break;
            }
        }
    }

}
