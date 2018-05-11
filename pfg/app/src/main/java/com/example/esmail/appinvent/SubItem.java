package com.example.esmail.appinvent;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SubItem extends AppCompatActivity {

    private String cBarras;
    private ArrayList<Item> datos = null;
    private SQLiteDatabase db;
    private BBDD_Helper baseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_item);
        setupActionBar();

        baseDatos = new BBDD_Helper(this);

        //Obtengo el codigo de barras
        Intent intent = getIntent();
        cBarras = intent.getStringExtra("itemSelected");

        try {
            db = baseDatos.getWritableDatabase();
            //Obtengo la fila entera
            String sql = "SELECT * FROM inventario WHERE CBarras=" + cBarras;
            Cursor c = db.rawQuery(sql, null);
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
                    datos.add(new Item("Codigo ERP:", codigo));
                    datos.add(new Item("Codigo de Barras:", cBarras));
                    datos.add(new Item("Descripcion:", descripcion));
                    datos.add(new Item("Unidades:", uni));
                    datos.add(new Item("Precio:", precio));
                    datos.add(new Item("Importe:", importe));
                } while (c.moveToNext());
            }

            //Muestro el arraylist en un listView
            if (datos != null) {
                ListView lv = (ListView) findViewById(R.id.lst_sub_item);
                AdaptadorSubItem adapter = new AdaptadorSubItem(this, datos);
                lv.setAdapter(adapter);
                //Haciendo un click largo permito editar los datos
                lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        String texto = ((Item) parent.getItemAtPosition(position)).getTexto();
                        String etiqueta = ((Item) parent.getItemAtPosition(position)).getEtiqueta();
                        String item = null;
                        //Obtengo el nombre de la columna dependiendo a que item pulse
                        if (etiqueta == "Codigo ERP:") item = BBDD.COLUMN_NAME_1;
                        if (etiqueta == "Codigo de Barras:") item = BBDD.COLUMN_NAME_2;
                        if (etiqueta == "Descripcion:") item = BBDD.COLUMN_NAME_3;
                        if (etiqueta == "Unidades:") item = BBDD.COLUMN_NAME_4;
                        if (etiqueta == "Precio:") item = BBDD.COLUMN_NAME_5;
                        if (etiqueta == "Importe:") item = BBDD.COLUMN_NAME_6;

                        ShowDialog(item);
                        return true;
                    }
                });
            } else {
                Toast.makeText(this, "No hay datos", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }


    }

    //flecha atras
    private void setupActionBar() {
        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.detalles));
        }
    }

    /**
     * Metodo que muestra un dialog con un editext y actualiza la base de datos con el contenido
     *
     * @param item
     */
    public void ShowDialog(String item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText editText = new EditText(this);
        builder.setTitle(R.string.tituloDialogoInventario);   // Título
        builder.setView(editText);
        final String finalItem = item;
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String texto = editText.getText().toString();
                GestionBBDD gestion = new GestionBBDD(getApplicationContext());
                //Actualizo la base de datos con lo que he obtenido del edittext
                if (finalItem != null && !texto.isEmpty()) {
                    String strSQL = "UPDATE inventario SET " + finalItem + " = " + texto + " WHERE CBarras = " + cBarras;
                    db = baseDatos.getWritableDatabase();
                    db.execSQL(strSQL);
                    finish();
                    startActivity(new Intent(SubItem.this, Iniciar.class));
                }

            }
        });
        builder.create();
        builder.show();
    }
}

