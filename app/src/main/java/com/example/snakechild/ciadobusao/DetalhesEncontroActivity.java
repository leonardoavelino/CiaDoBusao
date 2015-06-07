package com.example.snakechild.ciadobusao;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.snakechild.ciadobusao.util.BaseActivity;
import com.example.snakechild.ciadobusao.util.CustomizeFont;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class DetalhesEncontroActivity extends BaseActivity {

    //Atributos para Menu Lateral (Obrigatorios)
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private static String idEncontro = "";
    private static String nomeDono = "";
    private static String participantesEncontro = "";
    private TextView nomeDoEncontro, donoDoEncontro, linhaDoEncontro, refDoEncontro, dataDoEncontro, horaDoEncontro;
    private List<String> confirmadosPresenca = new ArrayList<String>();
    private List<String> estaoChegando = new ArrayList<String>();
    private ListView mListViewConfirmados, mListViewChegando;
    private ArrayAdapter<String> adapterConfirmados, adapterChegando;
    private ImageButton confirmaPresencaButton;
    private ImageButton saindoButton;
    private List<ParseObject> allUsers = new ArrayList<ParseObject>();

    public static void setEncontro(String encontro) {
        DetalhesEncontroActivity.idEncontro = encontro;
    }

    public void getDetalhesEncontro(){
        ParseQuery query = new ParseQuery("Encontro");
        query.whereEqualTo("objectId", idEncontro);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    nomeDoEncontro.setText((String) parseObjects.get(0).get("nome"));
                    linhaDoEncontro.setText((String) parseObjects.get(0).get("linha"));
                    refDoEncontro.setText((String) parseObjects.get(0).get("referencia"));
                    dataDoEncontro.setText((String) parseObjects.get(0).get("data"));
                    horaDoEncontro.setText((String) parseObjects.get(0).get("horario"));

                    ParseQuery query = new ParseQuery("Usuario");
                    query.whereEqualTo("id_user", (String) parseObjects.get(0).get("idDono"));
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            if (parseObjects != null) {
                                donoDoEncontro.setText((String) parseObjects.get(0).get("nome"));
                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_encontro);

        //Carrega o menu lateral
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);//load icons from strings.xml
        set(navMenuTitles, navMenuIcons);

        nomeDoEncontro = (TextView) findViewById(R.id.idDetalheNomeEncontro);
        donoDoEncontro = (TextView) findViewById(R.id.idDetalheDonoEncontro);
        linhaDoEncontro = (TextView) findViewById(R.id.idDetalheLinhaEncontro);
        refDoEncontro = (TextView) findViewById(R.id.idDetalheRefEncontro);
        dataDoEncontro = (TextView) findViewById(R.id.idDetalheDataEncontro);
        horaDoEncontro = (TextView) findViewById(R.id.idDetalheHoraEncontro);

        confirmaPresencaButton = (ImageButton) findViewById(R.id.idConfirmaButton);
        saindoButton = (ImageButton) findViewById(R.id.idSaindoButton);

        getDetalhesEncontro();
        customizeItems();


        getAllUsers();

        //Carrega a lista dos confirmados
        mListViewConfirmados = (ListView) findViewById(R.id.idConfirmadosPresencalistView);
        adapterConfirmados = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, confirmadosPresenca);
        mListViewConfirmados.setAdapter(adapterConfirmados);

        //Carrega a lista dos que estao chegando
        mListViewChegando = (ListView) findViewById(R.id.idEstaoChegandolistView);
        adapterChegando = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, estaoChegando);
        mListViewChegando.setAdapter(adapterChegando);

        getPerfis("PerfisACaminho", estaoChegando, adapterChegando);
        getPerfis("PerfisConfirmaram", confirmadosPresenca, adapterConfirmados);

        setButtonsVisible();

    }

    private void setButtonsVisible(){
        ParseQuery query = new ParseQuery("PerfisConfirmaram");
        query.whereEqualTo("idUsuario", PerfilActivity.idUsuario);
        query.whereEqualTo("idEncontro", idEncontro);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    if (!parseObjects.isEmpty()) {
                        ParseQuery query = new ParseQuery("PerfisACaminho");
                        query.whereEqualTo("idUsuario", PerfilActivity.idUsuario);
                        query.whereEqualTo("idEncontro", idEncontro);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects, ParseException e) {
                                if (parseObjects != null) {
                                    if (!parseObjects.isEmpty()) {
                                        saindoButton.setVisibility(View.INVISIBLE);
                                        saindoButton.setClickable(false);
                                    } else{
                                        saindoButton.setVisibility(View.VISIBLE);
                                        saindoButton.setClickable(true);
                                    }
                                }
                            }
                        });
                    }else{
                        confirmaPresencaButton.setVisibility(View.VISIBLE);
                        confirmaPresencaButton.setClickable(true);
                    }
                }
            }
        });
    }

    private void getPerfis(String table, final List<String> list, final ArrayAdapter<String> arrayAdapter){
        list.clear();
        ParseQuery query = new ParseQuery(table);
        query.whereEqualTo("idEncontro", idEncontro);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        for (ParseObject p: allUsers){
                            if (((String) parseObjects.get(i).get("idUsuario")).equals(((String) p.get("id_user")))){
                                list.add((String) p.get("nome"));
                            }
                        }

                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getAllUsers(){
        ParseQuery query = new ParseQuery("Usuario");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    //listView
                    allUsers = parseObjects;
                }
            }
        });
    }

    public void confirmaPresenca(View v) {
        final ParseObject usuario = new ParseObject("PerfisConfirmaram");
        usuario.put("idUsuario", PerfilActivity.idUsuario);
        usuario.put("idEncontro", idEncontro);
        usuario.saveInBackground();

        if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        } else {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    public void aCaminho(View v) {
        final ParseObject usuario = new ParseObject("PerfisACaminho");
        usuario.put("idUsuario", PerfilActivity.idUsuario);
        usuario.put("idEncontro", idEncontro);
        usuario.saveInBackground();

        if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        } else {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    public void customizeItems() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", nomeDoEncontro);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
