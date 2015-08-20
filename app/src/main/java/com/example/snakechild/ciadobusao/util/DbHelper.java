package com.example.snakechild.ciadobusao.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pedro on 20/08/2015.
 */
public class DbHelper extends SQLiteOpenHelper {


    private static final String NAME = "CIA_DO_BUSAO_BD";
    private static final int VERSION = 4;
    public static final String ENCONTRO = "ENCONTRO";
    public static final String DOM = "DOM";
    public static final String SEG = "SEG";
    public static final String TER = "TER";
    public static final String QUA = "QUA";
    public static final String QUI = "QUI";
    public static final String SEX = "SEX";
    public static final String SAB = "SAB";
    public static final String NOME = "NOME";
    public static final String HORA = "HORA";
    public static final String LAT = "LAT";
    public static final String LNG = "LNG";
    public static final String IDUSR = "IDUSR";
    public static final String LINHA = "LINHA";
    public static final String REFE = "REFE";

    public DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        criaTabela(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTabela(db);
        criaTabela(db);
    }

    private void criaTabela(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ENCONTRO
                + "(_id integer primary key autoincrement, "
                + IDUSR + " text, "
                + NOME + " text, "
                + HORA + " text, "
                + LAT + " text, "
                + LNG + " text, "
                + LINHA + " text, "
                + REFE + " text, "
                + DOM + " integer, "
                + SEG + " integer, "
                + TER + " integer, "
                + QUA + " integer, "
                + QUI + " integer, "
                + SEX + " integer, "
                + SAB + " integer);");
    }


    private void dropTabela(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + ENCONTRO);
    }
}
