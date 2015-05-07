package com.example.snakechild.ciadobusao.util;

import android.text.format.DateFormat;

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

    public static boolean isData(String data){
        if (data != null){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            Date actual = new Date(System.currentTimeMillis());
            try {
                date = sdf.parse(data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(date.after(actual)){
                return true;
            }
        }
        return false;
    }

    public static boolean isHorario(String horario){
        if(horario != null){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date date = new Date();
            Date actual = new Date(System.currentTimeMillis());
            try {
                date = sdf.parse(horario);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(date.after(actual)){
                return true;
            }
        }
        return false;
    }

}
