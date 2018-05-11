package com.example.esmail.appinvent;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.esmail.appinvent.fragments.ListInventFragment;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.example.esmail.appinvent.Iniciar.EXTRA_NOMBRE;


public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    private String eleccion;
    private String cantidad;
    private GestionBBDD gestion;
    private SQLiteDatabase db;
    private BBDD_Helper baseD;

    private TextView codBar, cant;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scan);

        Toolbar appbar = (Toolbar) findViewById(R.id.appbar_scan);
        setSupportActionBar(appbar);

        codBar = findViewById(R.id.txtCodBar);
        cant = findViewById(R.id.txtCant);

        gestion = new GestionBBDD(this);
        baseD = new BBDD_Helper(this);

        Intent intentResultado = getIntent();
        //recoge la eleccion
        eleccion = intentResultado.getStringExtra(EXTRA_NOMBRE);

        try {
            mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
            setContentView(mScannerView);                // Set the scanner view as the content view
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupActionBar();
    }

    //flecha atras
    private void setupActionBar() {
        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.scanner));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }



    @Override
    public void handleResult(Result result) {
        mScannerView.stopCameraPreview();
        mScannerView.resumeCameraPreview(this);

        switch (eleccion) {
            case "1":
                cantidad = "1";
                break;
            case "2":
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                final EditText editText = new EditText(this);
                builder.setTitle(R.string.tituloDialogoSaveData);   // Título
                builder.setView(editText);
                builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String aux = editText.getText().toString();
                        cantidad = aux;
                    }
                });
                builder.create();
                builder.show();
                break;
        }
        String resultado = result.getText();
        codBar.setText(resultado.toString());
        cant.setText(cantidad.toString());
        addDb(cantidad, resultado);

    }


        /**
         * Método para añadir productos a la bbdd
         *
         * @param cantidad
         * @param producto
         */
        public void addDb (String cantidad, String producto){
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

            if (db != null) {
                db.close();
            }

        }
    }



