package com.example.snakechild.ciadobusao;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.snakechild.ciadobusao.util.ControladorEncontroRecorrente;
import com.example.snakechild.ciadobusao.util.EncontroRecorrente;
import com.example.snakechild.ciadobusao.util.RecorrentListItem;
import com.example.snakechild.ciadobusao.util.RecorrenteAdapter;

import java.util.ArrayList;
import java.util.List;


public class EncontrosRecorrentesActivity extends Activity {

    ControladorEncontroRecorrente controladorEncontroRecorrente;
    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encontros_recorrentes);

        controladorEncontroRecorrente = ControladorEncontroRecorrente.getInstance(getApplicationContext());

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        criaLista();

    }

    private void criaLista() {
        lista = (ListView) findViewById(R.id.encontrosRecorrentesList);
        List<RecorrentListItem> itens = new ArrayList<RecorrentListItem>();
        for (EncontroRecorrente encontroRecorrente : controladorEncontroRecorrente.getEncontroRecorrenteList()) {
            String nome = encontroRecorrente.getNome();
            String hora = encontroRecorrente.getHora();
            Log.i("BRASIL", nome);
            String dias = "";
            for (int i = 0; i < encontroRecorrente.getDias().length; i++) {
                if (encontroRecorrente.getDias()[i]) {
                    dias = dias.concat(getDia(i) + " ");
                }
            }
            itens.add(new RecorrentListItem(nome, hora, dias));
        }
        lista.setAdapter(new RecorrenteAdapter(getApplicationContext(), itens));
    }

    private String getDia(int i) {
        String dia = "";
        if (i == 0) {
            dia = "DOM";
        } else if (i == 1) {
            dia = "SEG";
        } else if (i == 2) {
            dia = "TER";
        } else if (i == 3) {
            dia = "QUA";
        } else if (i == 4) {
            dia = "QUI";
        } else if (i == 5) {
            dia = "SEX";
        } else if (i == 6) {
            dia = "SAB";
        }
        return dia;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
