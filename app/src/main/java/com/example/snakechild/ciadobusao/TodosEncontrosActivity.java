package com.example.snakechild.ciadobusao;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.snakechild.ciadobusao.util.CustomizeFont;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class TodosEncontrosActivity extends Activity {

    private ListView mListView;
    private List<String> encontros = new ArrayList<String>();
    private List<String> idsEncontros = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos_encontros);

        title = (TextView) findViewById(R.id.tv_lista_title);

        customizeItems();

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Carrega a lista
        mListView = (ListView) findViewById(R.id.idTodosEncontroslistView);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, encontros);
        mListView.setAdapter(adapter);

        //Detalha um encontro
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idEncontro = getIdEncontro(position);
                Intent i = new Intent();
                i.setClass(getApplicationContext(), DetalhesEncontroActivity.class);
                startActivity(i);
                DetalhesEncontroActivity.setEncontro(idEncontro);
            }
        });
    }

    public void customizeItems() {
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", title);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void getEncontros() {
        ParseQuery query = new ParseQuery("Encontro");
        /**
         * Falta inserir a seguranca aqui!
         */
        query.whereNotEqualTo("idDono", PerfilActivity.idUsuario);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                encontros.clear();
                idsEncontros.clear();
                if (parseObjects != null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        String id = (String) parseObjects.get(i).getObjectId();
                        String encontro = (String) parseObjects.get(i).get("nome");
                        encontros.add(i, encontro);
                        idsEncontros.add(i, id);

                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_todos_encontros, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEncontros();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public String getIdEncontro(int position) {
        return idsEncontros.get(position);
    }

}
