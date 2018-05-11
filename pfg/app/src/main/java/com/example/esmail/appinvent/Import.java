package com.example.esmail.appinvent;

import android.content.ContentValues;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Import {

    private GestionBBDD gestion;
    private final static String NOMBRE_DOCUMENTO = "productos.csv";
    private File f;
    private Context context;

    public Import(GestionBBDD gestion, Context context) {
        this.gestion = gestion;
        this.context = context;
    }

    /**
     * Metodo que lee el fichero y
     * si esta  correcto rellena
     * la bbdd y la muestra en un listView
     */
    public void actionImport() {
        try {
            f = leerFichero(NOMBRE_DOCUMENTO);

            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String csvLine;

            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split("\t");
                System.out.println("ARRAY");
                for (int i = 0; i < row.length; i++) {
                    System.out.println(row[i]);
                }
                System.out.println("FIN");
                ArrayList<String> alAux = new ArrayList<>();
                for (int i = 0; i < row.length; i++) {
                    alAux.add(row[i]);
                }
                llenarBD(new Row(alAux.get(0), alAux.get(1), alAux.get(2), alAux.get(3), alAux.get(4), alAux.get(5)));
                alAux.clear();

            }
            reader.close();



        } catch (Exception ex) {
            Toast.makeText(context, "Error al leer fichero", Toast.LENGTH_LONG).show();
            Log.e("Ficheros", ex.getMessage());
        }

    }

    /**
     * Inserta los datos obtenido en la base de datos
     *
     * @param row
     */
    public void llenarBD(Row row) {
        String codigo = row.getCodigo();
        String cBarras = row.getCBarras();
        String descripcion = row.getDescripcion();
        String uni = row.getUnid();
        String precio = row.getPrecio();
        String importe = row.getImporte();

        //Si existe el codigo actualiza la descripcion y el precio
        if (gestion.consultar(BBDD.COLUMN_NAME_2, cBarras)) {
            ContentValues values = new ContentValues();
            values.put(BBDD.COLUMN_NAME_1, codigo);
            values.put(BBDD.COLUMN_NAME_3, descripcion);
            values.put(BBDD.COLUMN_NAME_5, precio);
            if (gestion.actualizar(BBDD.COLUMN_NAME_2, values, cBarras))
                Toast.makeText(context, "Se ha actualizado el registro", Toast.LENGTH_LONG).show();
        } else {    //sino lo inserta  lo que le llega
            gestion.insertar(codigo, cBarras, descripcion, uni, precio, importe);
        }
    }


    /**
     * Crea un fichero y obtiene la ruta
     *
     * @param nombreFichero
     * @return
     * @throws IOException
     */
    public static File leerFichero(String nombreFichero) throws IOException {
        File ruta = getRuta();
        System.out.println(ruta);
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero);
        return fichero;
    }

    /**
     * Metodo para obtener la ruta
     *
     * @return
     */
    public static File getRuta() {

        // El fichero ser? almacenado en un directorio dentro del directorio
        // Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            ruta = new File(
                    String.valueOf(Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null;
                    }
                }
            }
        } else {
        }

        return ruta;
    }
}
