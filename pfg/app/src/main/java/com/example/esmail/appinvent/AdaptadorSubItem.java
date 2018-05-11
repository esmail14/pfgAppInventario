package com.example.esmail.appinvent;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class AdaptadorSubItem extends BaseAdapter {

        protected Activity activity;
        protected ArrayList<Item> items;

        public AdaptadorSubItem(Activity activity, ArrayList<Item> items) {
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

        public void addAll(ArrayList<Item> lista){
            for (int i = 0; i < lista.size(); i++) {
                items.add(lista.get(i));
            }
        }

        @Override
        public Object getItem(int arg0) {
            return items.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (convertView == null){
                LayoutInflater inf=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.subitem_layout, null);
            }

            Item dir = items.get(position);

            TextView etiqueta=(TextView)v.findViewById(R.id.etiqueta);
            etiqueta.setText((dir.getEtiqueta()));

            TextView texto = (TextView) v.findViewById(R.id.texto);
            texto.setText(dir.getTexto());



            return v;
        }
    }
