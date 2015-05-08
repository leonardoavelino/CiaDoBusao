package com.example.snakechild.ciadobusao.util;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ValidateInput {

    public static boolean isPonto(String ponto){
        if (ponto.length() > 2 && ponto != null) {
            return true;
        }
        return false;
    }

    public static boolean isLinha(String linha) {
        if (linha.length() > 2 && linha != null) {
            return true;
        }
        return false;
    }

    public static boolean isNome(String nomeEncontro) {
        if (nomeEncontro.length() > 3 && nomeEncontro != null) {
            return true;
        }
        return false;
    }

    public static boolean isDataHora(String data, String hora){
        Date actual = new Date(System.currentTimeMillis());
        if (data != "" && hora != ""){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = new Date();
            try {
                date = sdf.parse(data + " " + hora);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.i("DATA",date.toString());
            Log.i("ACTUAL",actual.toString());
            if(!date.before(actual)){
                return true;
            }
        }
        return false;
    }
}
