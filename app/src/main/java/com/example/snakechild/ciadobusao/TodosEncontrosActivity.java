package com.example.snakechild.ciadobusao;

import android.content.res.TypedArray;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.snakechild.ciadobusao.util.BaseActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class TodosEncontrosActivity extends BaseActivity {

    //Atributos para Menu Lateral (Obrigatorios)
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ListView mListView;
    private List<String> encontros = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos_encontros);
        customizeItems();

        //Carrega o menu lateral
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);//load icons from strings.xml
        set(navMenuTitles, navMenuIcons);

        mListView = (ListView) findViewById(R.id.idTodosEncontroslistView);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, encontros);
        mListView.setAdapter(adapter);
    }

    public void customizeItems() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void getEncontros() {
        ParseQuery query = new ParseQuery("Encontro");
        query.whereNotEqualTo("idDono", PerfilActivity.idUsuario);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                encontros.clear();
                if (parseObjects != null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        String encontro = (String) parseObjects.get(i).get("nome");
                        encontros.add(i, encontro);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todos_encontros, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEncontros();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
