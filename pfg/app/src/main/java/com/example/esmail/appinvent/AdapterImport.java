package com.example.esmail.appinvent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdapterImport extends RecyclerView.Adapter<AdapterImport.MyViewHolder>{

    private List<Row> rowList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo, descripcion, unidades;

        public MyViewHolder(View view) {
            super(view);
            titulo = (TextView) view.findViewById(R.id.titulo);
            unidades = (TextView) view.findViewById(R.id.descripcion);
            descripcion = (TextView) view.findViewById(R.id.unidades);
        }
    }


    public AdapterImport(List<Row> rowList) {
        this.rowList = rowList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);

        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Row row = rowList.get(position);
        holder.titulo.setText(row.getCBarras());
        holder.unidades.setText(row.getDescripcion());
        holder.descripcion.setText(row.getUnid());
    }

    @Override
    public int getItemCount() {
        return rowList.size();
    }
}
