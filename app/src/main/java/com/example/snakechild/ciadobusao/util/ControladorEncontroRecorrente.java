package com.example.snakechild.ciadobusao.util;

import android.content.Context;

import com.example.snakechild.ciadobusao.PerfilActivity;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Pedro on 19/08/2015.
 */
public class ControladorEncontroRecorrente {

    private static ControladorEncontroRecorrente instance;
    List<EncontroRecorrente> encontroRecorrenteList;
    private DataSource dataSource;
    private int diaHojeSemana;

    private ControladorEncontroRecorrente(Context context) {
        dataSource = new DataSource(context);
        dataSource.open();
        encontroRecorrenteList = dataSource.getTodosEncontros();
        if (encontroRecorrenteList.size() > 0) {
            criaEncontrosDoDia();
        }
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

    private void criaEncontrosDoDia() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String dataFormatada = format.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        diaHojeSemana = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        for (EncontroRecorrente encontroRecorrente: encontroRecorrenteList) {
            if (encontroRecorrente.getDias()[diaHojeSemana]) {
                if (!horaJaPassou(dataFormatada,encontroRecorrente.getHora())) {
                    ParseObject encontro = new ParseObject("Encontro");
                    String latitudeString = String.valueOf(encontroRecorrente.getLat());
                    String longitudeString = String.valueOf(encontroRecorrente.getLng());

                    encontro.put("data", dataFormatada);
                    encontro.put("horario", encontroRecorrente.getHora());
                    encontro.put("idDono", encontroRecorrente.getIdUsr());
                    encontro.put("latitude", latitudeString);
                    encontro.put("longitude", longitudeString);
                    encontro.put("linha", encontroRecorrente.getLinha());
                    encontro.put("nome", encontroRecorrente.getNome());
                    encontro.put("referencia", encontroRecorrente.getReferencia());
                    encontro.saveInBackground();
                }
            }
        }

    }

    private boolean horaJaPassou(String data, String hora) {
        return !ValidateInput.isDataHora(data,hora);
    }

}
