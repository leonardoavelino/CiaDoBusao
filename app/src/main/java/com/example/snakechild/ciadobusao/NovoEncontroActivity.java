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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novoencontro);

        nomeText = (TextView) this.findViewById(R.id.idNomeEnc);
        linhaText = (TextView) this.findViewById(R.id.idLinhaOnibusText);
        pontoRefText = (TextView) this.findViewById(R.id.idPontoRefText);
        customizeItems();
    }

    public void customizeItems(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", nomeText);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", linhaText);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", pontoRefText);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
