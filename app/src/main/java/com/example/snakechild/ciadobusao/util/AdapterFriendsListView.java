package com.example.snakechild.ciadobusao.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.snakechild.ciadobusao.R;

import java.util.List;

public class AdapterFriendsListView extends BaseAdapter {

    private Context context;
    private List<String> itens; //ArrayList<NavDrawerItem>

    public AdapterFriendsListView(Context context, List<String> itens) {
        this.context = context;
        this.itens = itens;
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Object getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.encontro_item_list, null);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.idItemList);
        txtTitle.setText(itens.get(position));
        Typeface typeFont = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
        txtTitle.setTypeface(typeFont);

        //ImageView imgUser = (ImageView) convertView.findViewById(R.id.icon); //CONSULTA?
        //imgIcon.setImageResource(itens.get(position).getIcon());

        return convertView;

    }

}