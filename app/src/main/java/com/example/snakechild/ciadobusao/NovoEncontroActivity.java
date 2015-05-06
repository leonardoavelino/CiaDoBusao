package com.example.snakechild.ciadobusao;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snakechild.ciadobusao.util.CustomizeFont;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class NovoEncontroActivity extends Activity {

    private static TextView nomeText, linhaText, pontoRefText, editDataText, editHoraText, editLocalMapaText, criarText, cancelarText;
    private static EditText nomeEncEditText, linhaEncEditTex, refEncEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novoencontro);

        nomeText = (TextView) this.findViewById(R.id.idNomeEnc);
        linhaText = (TextView) this.findViewById(R.id.idLinhaOnibusText);
        pontoRefText = (TextView) this.findViewById(R.id.idPontoRefText);
        editDataText = (TextView) this.findViewById(R.id.idEditarDataTextView);
        editHoraText = (TextView) this.findViewById(R.id.idEditarHoraTextView);
        editLocalMapaText = (TextView) this.findViewById(R.id.idEditarMapaTextView);
        criarText = (TextView) this.findViewById(R.id.idCriarTextView);
        cancelarText = (TextView) this.findViewById(R.id.idCancelarTextView);

        nomeEncEditText = (EditText) this.findViewById(R.id.idNomeEdit);
        linhaEncEditTex = (EditText) this.findViewById(R.id.idLinhaEdit);
        refEncEditText = (EditText) this.findViewById(R.id.idPontoEdit);

        customizeItems();
    }

    public void customizeItems(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", nomeText);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", linhaText);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", pontoRefText);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", editDataText);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", editHoraText);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", editLocalMapaText);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", criarText);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", cancelarText);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onBack(View v){
        onBackPressed();
        finish();
    }

    public void saveEncontro(View v){
        ParseObject encontro = new ParseObject("Encontro");

        encontro.put("data", "DD-MM-YYYY");
        encontro.put("horario", "HH:MM");
        encontro.put("idDono", PerfilActivity.idUsuario);
        encontro.put("latitude", "");
        encontro.put("longitude", "");
        encontro.put("linha", linhaEncEditTex.getText().toString());
        encontro.put("nome", nomeEncEditText.getText().toString());
        encontro.put("referencia", refEncEditText.getText().toString());
        encontro.saveInBackground();
        onBack(v);
    }
}
