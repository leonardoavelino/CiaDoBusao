package com.example.snakechild.ciadobusao;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snakechild.ciadobusao.util.BaseActivity;
import com.example.snakechild.ciadobusao.util.CustomizeFont;
import com.example.snakechild.ciadobusao.util.LocationService;
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
    private TextView nomeNome, donoDono, linhaLinha, refRef, dataData, horaHora, confirma, saindo, chegaramMaps;
    private List<String> confirmadosPresenca = new ArrayList<String>();
    private List<String> estaoChegando = new ArrayList<String>();
    private List<String> chegaram = new ArrayList<String>();
    private ListView mListViewConfirmados, mListViewChegando, mListViewChegaram;
    private ArrayAdapter<String> adapterConfirmados, adapterChegando, adapterChegaram;
    private ImageButton confirmaPresencaButton;
    private ImageButton saindoButton;
    private List<ParseObject> allUsers = new ArrayList<ParseObject>();
    private boolean participo = false;
    private String encontroLat;
    private String encontroLong;

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
                    encontroLat = (String) parseObjects.get(0).get("latitude");
                    encontroLong = (String) parseObjects.get(0).get("longitude");

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

        getAllUsers();

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

        nomeNome = (TextView) findViewById(R.id.idDetalheNomeTextView);
        donoDono = (TextView) findViewById(R.id.idDetalheDonoTextView);
        linhaLinha = (TextView) findViewById(R.id.idDetalheLinhaTextView);
        refRef = (TextView) findViewById(R.id.idDetalheRefTextView);
        dataData = (TextView) findViewById(R.id.idDetalheDataTextView);
        horaHora = (TextView) findViewById(R.id.idDetalheHoraTextView);
        confirma = (TextView) findViewById(R.id.idDetalheConfirmaTextView);
        saindo = (TextView) findViewById(R.id.idDetalheSaindoTextView);
        chegaramMaps = (TextView) findViewById(R.id.chegouText);

        confirmaPresencaButton = (ImageButton) findViewById(R.id.idConfirmaButton);
        saindoButton = (ImageButton) findViewById(R.id.idSaindoButton);

        getDetalhesEncontro();
        customizeItems();


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

        //Carrega a lista dos que chegaram
        mListViewChegaram = (ListView) findViewById(R.id.chegouList);
        adapterChegaram = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, chegaram);
        mListViewChegaram.setAdapter(adapterChegaram);

        getPerfis("PerfisACaminho", estaoChegando, adapterChegando);
        getPerfis("PerfisConfirmaram", confirmadosPresenca, adapterConfirmados);
        getPerfis("PerfisChegaram", chegaram, adapterChegaram);

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
                        participo = true;
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

    private void getPerfis(final String table, final List<String> list, final ArrayAdapter<String> arrayAdapter){
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
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.confirma_presenca), Toast.LENGTH_SHORT).show();
    }

    public void aCaminho(View v) {
        final ParseObject usuario = new ParseObject("PerfisACaminho");
        usuario.put("idUsuario", PerfilActivity.idUsuario);
        usuario.put("idEncontro", idEncontro);
        usuario.saveInBackground();
        // ----- Inicia Service passando o numero do usuario
        Intent servIt = new Intent(getApplicationContext(), LocationService.class);
        Bundle bundle = new Bundle();
        bundle.putString("idUsuario", PerfilActivity.idUsuario);
        bundle.putString("idEncontro", idEncontro);
        bundle.putString("encontroLat", encontroLat);
        bundle.putString("encontroLong", encontroLong);
        servIt.putExtras(bundle);
        startService(servIt);
        // ----

        if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        } else {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.confirma_saida), Toast.LENGTH_SHORT).show();
    }

    public void customizeItems() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CustomizeFont.customizeFont(this, "Roboto-Bold.ttf", nomeNome);
        CustomizeFont.customizeFont(this, "Roboto-Bold.ttf", dataData);
        CustomizeFont.customizeFont(this, "Roboto-Bold.ttf", horaHora);
        CustomizeFont.customizeFont(this, "Roboto-Bold.ttf", refRef);
        CustomizeFont.customizeFont(this, "Roboto-Bold.ttf", linhaLinha);
        CustomizeFont.customizeFont(this, "Roboto-Bold.ttf", confirma);
        CustomizeFont.customizeFont(this, "Roboto-Bold.ttf", donoDono);
        CustomizeFont.customizeFont(this, "Roboto-Bold.ttf", saindo);
        CustomizeFont.customizeFont(this, "Roboto-Bold.ttf", chegaramMaps);
        CustomizeFont.customizeFont(this, "Roboto-Regular.ttf", nomeDoEncontro);
        CustomizeFont.customizeFont(this, "Roboto-Regular.ttf", donoDoEncontro);
        CustomizeFont.customizeFont(this, "Roboto-Regular.ttf", linhaDoEncontro);
        CustomizeFont.customizeFont(this, "Roboto-Regular.ttf", refDoEncontro);
        CustomizeFont.customizeFont(this, "Roboto-Regular.ttf", dataDoEncontro);
        CustomizeFont.customizeFont(this, "Roboto-Regular.ttf", horaDoEncontro);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        ParseQuery query = new ParseQuery("PerfisConfirmaram");
        query.whereEqualTo("idEncontro", idEncontro);
        query.whereEqualTo("idUsuario", PerfilActivity.idUsuario);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        if (parseObjects.get(i).get("idUsuario").equals(PerfilActivity.idUsuario)) {
                            getMenuInflater().inflate(R.menu.menu_detalhes_encontro, menu);
                        }
                    }
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_excluir_encontro) {
            new AlertDialog.Builder(this)
                .setTitle(getString(R.string.str_title_dialog_excluir_encontro))
                .setMessage(getString(R.string.str_description_excluir_encontro))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (adapterConfirmados.getCount()==1){
                            removeEncontro(idEncontro);
                        }
                        if (!adapterConfirmados.isEmpty()) {
                            desconfirmaConfirmaram(idEncontro);
                        }
                        if (!adapterChegando.isEmpty()){
                            desconfirmaACaminho(idEncontro);
                        }
                        onBackPressed();
                    }
                })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
            }
        return true;
    }


    public void removeEncontro(String idEncontro){
        ParseQuery query = new ParseQuery("Encontro");
        query.whereEqualTo("objectId", idEncontro);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    parseObjects.get(0).deleteInBackground();
                }
            }
        });
    }

    public void desconfirmaACaminho(String idEncontro){
        ParseQuery query = new ParseQuery("PerfisACaminho");
        query.whereEqualTo("idEncontro", idEncontro);
        query.whereEqualTo("idUsuario", PerfilActivity.idUsuario);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    parseObjects.get(0).deleteInBackground();
                }
            }
        });
    }//venha simbora deixa isso

    public void desconfirmaConfirmaram(String idEncontro){
        ParseQuery query = new ParseQuery("PerfisConfirmaram");
        query.whereEqualTo("idEncontro", idEncontro);
        query.whereEqualTo("idUsuario", PerfilActivity.idUsuario);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    parseObjects.get(0).deleteInBackground();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
