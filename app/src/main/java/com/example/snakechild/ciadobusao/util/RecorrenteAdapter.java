package com.example.snakechild.ciadobusao.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.snakechild.ciadobusao.R;

import java.util.List;

/**
 * Created by Pedro on 19/08/2015.
 */
public class RecorrenteAdapter extends BaseAdapter {

    private Context context;
    private List<RecorrentListItem> navDrawerItems;
    private ControladorEncontroRecorrente controladorEncontroRecorrente;

    public RecorrenteAdapter(Context context, List<RecorrentListItem> navDrawerItems) {
        controladorEncontroRecorrente = ControladorEncontroRecorrente.getInstance(context);
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.recorrente_list_item, null);
        }
        TextView nome = (TextView) convertView.findViewById(R.id.nomeEncontro);
        TextView diasSemana = (TextView) convertView.findViewById(R.id.diasDaSemana);

        nome.setText(navDrawerItems.get(position).getNome() + " - " + navDrawerItems.get(position).getHora());
        diasSemana.setText(navDrawerItems.get(position).getDiasSemana());
        Typeface typeFont = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
        nome.setTypeface(typeFont);
        diasSemana.setTypeface(typeFont);
        nome.setTextColor(context.getResources().getColor(R.color.list_item_title));
        diasSemana.setTextColor(context.getResources().getColor(R.color.list_item_title));

        Button removeButton = (Button) convertView.findViewById(R.id.apagarButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controladorEncontroRecorrente.removeEncontro(navDrawerItems.get(position).getNome());
                navDrawerItems.remove(position);
                RecorrenteAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
