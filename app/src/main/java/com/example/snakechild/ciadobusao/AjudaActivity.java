package com.example.snakechild.ciadobusao;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.snakechild.ciadobusao.util.BaseActivity;
import com.example.snakechild.ciadobusao.util.CustomizeFont;


public class AjudaActivity extends Activity {

    //Atributos para Menu Lateral (Obrigatorios)
    //private String[] navMenuTitles;
    //private TypedArray navMenuIcons;

    private static TextView descText, chameText, chameDescText, marqueText, marqueDescText, naoVaText, naoVaDescText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);

        descText = (TextView) this.findViewById(R.id.idDescricaoApp);
        chameText = (TextView) this.findViewById(R.id.idChame);
        chameDescText = (TextView) this.findViewById(R.id.idChameDescricao);
        marqueText = (TextView) this.findViewById(R.id.idMarque);
        marqueDescText = (TextView) this.findViewById(R.id.idMarqueDescricao);
        naoVaText = (TextView) this.findViewById(R.id.idNaoVa);
        naoVaDescText = (TextView) this.findViewById(R.id.idNaoVaDescricao);

        customizeItems();

        //Carrega o menu lateral
        /*navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);//load icons from strings.xml
        set(navMenuTitles, navMenuIcons);*/
    }

    public void customizeItems() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CustomizeFont.customizeFont(this, "Amaranth-Bold.otf", chameText);
        CustomizeFont.customizeFont(this, "Amaranth-Bold.otf", marqueText);
        CustomizeFont.customizeFont(this, "Amaranth-Bold.otf", naoVaText);

        CustomizeFont.customizeFont(this, "Roboto-Regular.ttf", descText);
        CustomizeFont.customizeFont(this, "Roboto-Regular.ttf", chameDescText);
        CustomizeFont.customizeFont(this, "Roboto-Regular.ttf", marqueDescText);
        CustomizeFont.customizeFont(this, "Roboto-Regular.ttf", naoVaDescText);
    }
}
