package com.example.esmail.appinvent;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class SaveData {

    private String c_barras_simple = null;
    private String c_barras_varios = null;
    private String c_barras_simple_scaner = null;
    private String c_barras_varios_scaner = null;

    public final static String EXTRA_NOMBRE = "com.example.esmail.inventario.unovarios.esmail";
    private GestionBBDD gestion;
    private SQLiteDatabase db;
    private BBDD_Helper baseD;





    /**
     * metodo para añadir un elemento
     */
    public void anadirUnElemento() {

        String cantidad = "1";
        if (c_barras_simple.length() < 13) {
           // c_barras_simple = obtenerCodigoBarrasCompleto(c_barras_simple);
        }
        addDb(cantidad, c_barras_simple);
    }


    /**
     * Metodo para añadir varios elementos
     */
    public void anadirVariosElementos() {
/*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (c_barras_varios.length() < 13) {
            c_barras_varios = obtenerCodigoBarrasCompleto(c_barras_varios);
        }
        final EditText editText = new EditText(this);
        builder.setTitle(R.string.tituloDialogoSaveData);   // Título
        builder.setView(editText);
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String cantidad = editText.getText().toString();
                addDb(cantidad, c_barras_varios);

            }
        });
        builder.create();
        builder.show();*/
    }



    /**
     * Método para añadir productos a la bbdd
     *
     * @param cantidad
     * @param producto
     */
    public void addDb(String cantidad, String producto) {
        int cant = Integer.parseInt(cantidad);

        if (gestion.consultar(BBDD.COLUMN_NAME_2, producto)) {      //busca el producto en la base de datos
            String sql = "SELECT Codigo, Cbarras, Descripcion, Unid, Precio, Importe FROM " + BBDD.TABLE_NAME + " WHERE Cbarras = '" + producto + "'";

            db = baseD.getWritableDatabase();
            Cursor c = db.rawQuery(sql, null);
            if (c.moveToFirst()) {
                do {
                    String codigo = c.getString(0);
                    String cBarras = c.getString(1);
                    String descripcion = c.getString(2);
                    String unid = c.getString(3);
                    String precio = c.getString(4);
                    String importe = c.getString(5);
                    //se suman las unidades
                    cant = Integer.parseInt(unid) + cant;
                    unid = String.valueOf(cant);
                    //guardamos los valores
                    ContentValues values = new ContentValues();
                    values.put(BBDD.COLUMN_NAME_1, codigo);
                    values.put(BBDD.COLUMN_NAME_2, cBarras);
                    values.put(BBDD.COLUMN_NAME_3, descripcion);
                    values.put(BBDD.COLUMN_NAME_4, unid);
                    values.put(BBDD.COLUMN_NAME_5, precio);
                    values.put(BBDD.COLUMN_NAME_6, importe);
                    //llama al metodo actualizar
                    gestion.actualizar(BBDD.COLUMN_NAME_2, values, cBarras);
                } while (c.moveToNext());
            }
        } else {
            //llama al metodo insertar
            gestion.insertar(null, producto, null, cantidad, null, null);
        }

        //inicia el metodo mostrar
        mostrar("", producto, "", cantidad, "", "");
        if (db != null) {
            db.close();
        }

    }

    /**
     * Método para ver los elementos de la bbdd
     */
    public void mostrar(String codigo, String cBarras, String descripcion, String unidades, String precio, String importe) {
/*
        ArrayList<Row> al = new ArrayList<>();
        //añadimos al arraylist lo que nos llega
        al.add(new Row(codigo, cBarras, descripcion, unidades, precio, importe));
        //instancia del listview
        ListView lv = getClass().findViewById(R.id.listViewData);

        AdapterItem adapter = new AdapterItem(this, al);
        lv.setAdapter(adapter);*/
    }
}
