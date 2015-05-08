package com.example.snakechild.ciadobusao.util;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class AdapterMeusEncontrosListView  extends ArrayAdapter<String> {

    private List<String> itens;

    public AdapterMeusEncontrosListView(Context context, int textViewResourceId, List<String> itens) {
        super(context, textViewResourceId, itens);
        this.itens = itens;
    }

}