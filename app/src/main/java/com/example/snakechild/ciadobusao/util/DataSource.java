package com.example.snakechild.ciadobusao.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pedro on 20/08/2015.
 */
public class DataSource {

    private SQLiteDatabase database;
    private DbHelper dbHelper;

    public DataSource(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public List<EncontroRecorrente> getTodosEncontros() {
        List<EncontroRecorrente> result = new ArrayList<EncontroRecorrente>();
        Log.i("DBHELPER", "PEGANDO TODOS OS ENCONTROS");
        String[] nameCollumn = {DbHelper.IDUSR, DbHelper.NOME, DbHelper.HORA, DbHelper.LAT, DbHelper.LNG,
                DbHelper.LINHA, DbHelper.REFE, DbHelper.DOM, DbHelper.SEG, DbHelper.TER, DbHelper.QUA,
                DbHelper.QUI, DbHelper.SEX, DbHelper.SAB};
        Cursor cursor = database.query(DbHelper.ENCONTRO, nameCollumn, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String idUsr = cursor.getString(0);
            String nome = cursor.getString(1);
            String hora = cursor.getString(2);
            String lat = cursor.getString(3);
            String lng = cursor.getString(4);
            String linha = cursor.getString(5);
            String ref = cursor.getString(6);
            boolean[] diasSemana = new boolean[7];
            diasSemana[0] = (cursor.getInt(7) == 1);
            diasSemana[1] = (cursor.getInt(8) == 1);
            diasSemana[2] = (cursor.getInt(9) == 1);
            diasSemana[3] = (cursor.getInt(10) == 1);
            diasSemana[4] = (cursor.getInt(11) == 1);
            diasSemana[5] = (cursor.getInt(12) == 1);
            diasSemana[6] = (cursor.getInt(13) == 1);
            result.add(new EncontroRecorrente(idUsr, nome, hora, lat, lng, diasSemana, linha, ref));
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public void insertEncontro(EncontroRecorrente encontroRecorrente) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.IDUSR, encontroRecorrente.getIdUsr());
        contentValues.put(DbHelper.NOME, encontroRecorrente.getNome());
        contentValues.put(DbHelper.HORA, encontroRecorrente.getHora());
        contentValues.put(DbHelper.LAT, encontroRecorrente.getLat());
        contentValues.put(DbHelper.LNG, encontroRecorrente.getLng());
        contentValues.put(DbHelper.LINHA, encontroRecorrente.getLinha());
        contentValues.put(DbHelper.REFE, encontroRecorrente.getReferencia());
        contentValues.put(DbHelper.DOM, (encontroRecorrente.getDias()[0] ? 1 : 0));
        contentValues.put(DbHelper.SEG, (encontroRecorrente.getDias()[1] ? 1 : 0));
        contentValues.put(DbHelper.TER, (encontroRecorrente.getDias()[2] ? 1 : 0));
        contentValues.put(DbHelper.QUA, (encontroRecorrente.getDias()[3] ? 1 : 0));
        contentValues.put(DbHelper.QUI, (encontroRecorrente.getDias()[4] ? 1 : 0));
        contentValues.put(DbHelper.SEX, (encontroRecorrente.getDias()[5] ? 1 : 0));
        contentValues.put(DbHelper.SAB, (encontroRecorrente.getDias()[6] ? 1 : 0));

        database.insert(DbHelper.ENCONTRO, null, contentValues);
    }

    public void removeEncontro(EncontroRecorrente encontroRecorrente) {
        database.delete(DbHelper.ENCONTRO, DbHelper.NOME + "='" + encontroRecorrente.getNome() + "'", null);
    }
}
