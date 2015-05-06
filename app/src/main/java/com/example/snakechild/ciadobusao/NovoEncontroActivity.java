package com.example.snakechild.ciadobusao;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.snakechild.ciadobusao.util.CustomizeFont;


public class NovoEncontroActivity extends Activity {

    private static TextView nomeText;
    private static TextView linhaText;
    private static TextView pontoRefText;
    private static TextView editDataText;
    private static TextView editHoraText;
    private static TextView editLocalMapaText;

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
