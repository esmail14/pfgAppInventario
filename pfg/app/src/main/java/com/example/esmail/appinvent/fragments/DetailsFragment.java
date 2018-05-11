package com.example.esmail.appinvent.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.esmail.appinvent.AdaptadorSubItem;
import com.example.esmail.appinvent.BBDD;
import com.example.esmail.appinvent.BBDD_Helper;
import com.example.esmail.appinvent.GestionBBDD;
import com.example.esmail.appinvent.Item;
import com.example.esmail.appinvent.R;

import java.util.ArrayList;


public class DetailsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String cBarras;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Obtengo el codigo de barras
        cBarras = getArguments().getString("cod-barras");
        System.out.println(cBarras);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BBDD_Helper baseDatos = new BBDD_Helper(getActivity().getApplicationContext());

        ListView lv = getActivity().findViewById(R.id.lst_sub_item);

        SQLiteDatabase db = null;
        try {
            db = baseDatos.getWritableDatabase();
            //Obtengo la fila entera
            String sql = "SELECT * FROM inventario WHERE CBarras='" + cBarras + "'";
            Cursor c = db.rawQuery(sql, null);
            ArrayList<Item> datos = null;
            if (c.moveToFirst()) {
                do {
                    String codigo = c.getString(1);
                    String cBarras = c.getString(2);
                    String descripcion = c.getString(3);
                    String uni = c.getString(4);
                    String precio = c.getString(5);
                    String importe = c.getString(6);
                    datos = new ArrayList<Item>();
                    //Inserto la fila en un arraylist
                    datos.add(new Item("Código ERP:", codigo));
                    datos.add(new Item("Código de Barras:", cBarras));
                    datos.add(new Item("Descripción:", descripcion));
                    datos.add(new Item("Unidades:", uni));
                    datos.add(new Item("Precio:", precio));
                    datos.add(new Item("Importe:", importe));
                } while (c.moveToNext());
            }
            c.close();

            //Muestro el arraylist en un listView
            if (datos != null) {
                AdaptadorSubItem adapter = new AdaptadorSubItem(getActivity(), datos);
                lv.setAdapter(adapter);
                //Haciendo un click largo permito editar los datos
                lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        String texto = ((Item) parent.getItemAtPosition(position)).getTexto();
                        String etiqueta = ((Item) parent.getItemAtPosition(position)).getEtiqueta();
                        String item = null;
                        //Obtengo el nombre de la columna dependiendo a que item pulse
                        if (etiqueta == "Código ERP:") item = BBDD.COLUMN_NAME_1;
                        if (etiqueta == "Código de Barras:") item = BBDD.COLUMN_NAME_2;
                        if (etiqueta == "Descripción:") item = BBDD.COLUMN_NAME_3;
                        if (etiqueta == "Unidades:") item = BBDD.COLUMN_NAME_4;
                        if (etiqueta == "Precio:") item = BBDD.COLUMN_NAME_5;
                        if (etiqueta == "Importe:") item = BBDD.COLUMN_NAME_6;

                        ShowDialog(item);
                        return true;
                    }
                });
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "No hay datos", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void ShowDialog(String item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());

        final EditText editText = new EditText(getActivity().getApplicationContext());
        builder.setTitle(R.string.tituloDialogoInventario);   // Título
        builder.setView(editText);
        final String finalItem = item;
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String texto = editText.getText().toString();
                GestionBBDD gestion = new GestionBBDD(getActivity().getApplicationContext());
                //Actualizo la base de datos con lo que he obtenido del edittext
                if (finalItem != null && !texto.isEmpty()) {
                    String strSQL = "UPDATE inventario SET " + finalItem + " = " + texto + " WHERE CBarras = " + cBarras;
                    SQLiteOpenHelper baseDatos = null;
                    SQLiteDatabase db = baseDatos.getWritableDatabase();
                    db.execSQL(strSQL);
                    Bundle args = new Bundle();
                    args.putString("cod-barras", cBarras);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    DetailsFragment detailsFragment = new DetailsFragment();
                    detailsFragment.setArguments(args);
                    fragmentTransaction.detach(detailsFragment).attach(detailsFragment).commit();
                }

            }
        });
        builder.create();
        builder.show();
    }




}
