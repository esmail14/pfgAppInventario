package com.example.esmail.appinvent;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterItem extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Row> items;

    public AdapterItem() {
        super();
    }


    public AdapterItem(Activity activity, ArrayList<Row> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear(){
        items.clear();
    }

    public void addAll(ArrayList<Row> lista){
        for (int i = 0; i < lista.size(); i++) {
            items.add(lista.get(i));
        }
    }


    @Override
    public Object getItem(int args0) {
        return items.get(args0);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null){
            LayoutInflater inf=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_layout, null);
        }

        Row dir = items.get(position);

        TextView cod_barras = (TextView) v.findViewById(R.id.titulo);
        cod_barras.setText(dir.getCBarras());

        TextView des=(TextView) v.findViewById(R.id.descripcion);
        des.setText(dir.getDescripcion());

        TextView uni = (TextView) v.findViewById(R.id.unidades);
        uni.setText(dir.getUnid());
        
        return v;
    }
}

