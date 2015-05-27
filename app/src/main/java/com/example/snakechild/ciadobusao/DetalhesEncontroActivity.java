package com.example.snakechild.ciadobusao;

import android.content.Intent;
import android.content.res.TypedArray;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.snakechild.ciadobusao.util.BaseActivity;
import com.example.snakechild.ciadobusao.util.CustomizeFont;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class DetalhesEncontroActivity extends BaseActivity {

    //Atributos para Menu Lateral (Obrigatorios)
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private static String idEncontro = "";
    private static String nomeDono = "";
    private static String participantesEncontro = "";
    private TextView nomeDoEncontro, donoDoEncontro, linhaDoEncontro, refDoEncontro, dataDoEncontro, horaDoEncontro;
    private TextView participantes;
    private Button confirmaPresencaButton, saindoButton;

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
                    donoDoEncontro.setText(getDonoDoEncontro((String) parseObjects.get(0).get("idDono")));

                }
            }
        });
    }

    public String getDonoDoEncontro(String idDono) {
        ParseQuery query = new ParseQuery("Usuario");
        query.whereEqualTo("id_user", idDono);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    nomeDono = (String) parseObjects.get(0).get("nome");
                }
            }
        });
        return nomeDono;
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
        participantes = (TextView) findViewById(R.id.idConfirmaram);

        confirmaPresencaButton = (Button) findViewById(R.id.idConfirmaButton);
        saindoButton = (Button) findViewById(R.id.idSaindoButton);

        customizeItems();
        getDetalhesEncontro();
        setButtonsVisible();
        getPerfisConfirmaram();
    }

    private void setButtonsVisible(){
        ParseQuery query = new ParseQuery("PerfisConfirmaram");
        query.whereEqualTo("idUsuario", PerfilActivity.idUsuario);
        query.whereEqualTo("idEncontro", idEncontro);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    if (!parseObjects.isEmpty()){
                        confirmaPresencaButton.setVisibility(View.INVISIBLE);
                        confirmaPresencaButton.setClickable(false);
                        saindoButton.setVisibility(View.VISIBLE);
                        saindoButton.setClickable(true);
                    }
                }
            }
        });
    }

    private void getPerfisConfirmaram(){
        ParseQuery query = new ParseQuery("PerfisConfirmaram");
        query.whereEqualTo("idEncontro", idEncontro);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        ParseQuery query = new ParseQuery("Usuario");
                        query.whereEqualTo("id_user", (String) parseObjects.get(i).get("idUsuario"));
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects, ParseException e) {
                                if (parseObjects != null) {
                                    participantesEncontro = participantesEncontro + (String) parseObjects.get(0).get("nome") + ",";
                                }
                            }
                        });
                    }
                }
            }
        });
        participantes.setText(participantesEncontro);
        participantesEncontro="";
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
