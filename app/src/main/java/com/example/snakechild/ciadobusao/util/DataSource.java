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
        String[] nameCollumn = {DbHelper.NOME, DbHelper.HORA, DbHelper.DOM,
                DbHelper.SEG, DbHelper.TER, DbHelper.QUA, DbHelper.QUI,
                DbHelper.SEX, DbHelper.SAB};
        Cursor cursor = database.query(DbHelper.ENCONTRO, nameCollumn, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String nome = cursor.getString(0);
            String hora = cursor.getString(1);
            boolean[] diasSemana = new boolean[7];
            diasSemana[0] = (cursor.getInt(2) == 1);
            diasSemana[1] = (cursor.getInt(3) == 1);
            diasSemana[2] = (cursor.getInt(4) == 1);
            diasSemana[3] = (cursor.getInt(5) == 1);
            diasSemana[4] = (cursor.getInt(6) == 1);
            diasSemana[5] = (cursor.getInt(7) == 1);
            diasSemana[6] = (cursor.getInt(8) == 1);
            result.add(new EncontroRecorrente(nome, hora, diasSemana));
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public void insertEncontro(EncontroRecorrente encontroRecorrente) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.NOME, encontroRecorrente.getNome());
        contentValues.put(DbHelper.HORA, encontroRecorrente.getHora());
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
