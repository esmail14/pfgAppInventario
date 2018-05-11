package com.example.esmail.appinvent;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Export {

    private GestionBBDD gestion;
    private static String NOMBRE_DOCUMENTO = null;
    private Context context;

    public Export(GestionBBDD gestion, Context context) {
        this.gestion = gestion;
        this.context = context;
    }

    /**
     * Exportamos el la base de datos a un fichero csv
     */
    public void exportarBD(String texto) {
        if (!texto.contains(".csv"))
            NOMBRE_DOCUMENTO = texto + ".csv";
        else NOMBRE_DOCUMENTO = texto;

        ArrayList<Row> al = gestion.verDB();

        try {
            File f = leerFichero(NOMBRE_DOCUMENTO);

            OutputStreamWriter fout =
                    new OutputStreamWriter(
                            new FileOutputStream(f));


            for (int i = 0; i < al.size(); i++) {

                String datos = al.get(i).getCodigo() + "\t" + al.get(i).getCBarras()
                        + "\t" + al.get(i).getDescripcion() + "\t" + al.get(i).getUnid()
                        + "\t" + al.get(i).getPrecio() + "\t" + al.get(i).getImporte() + "\r\n";
                System.out.println(datos);
                fout.write(datos);
                Toast.makeText(context, "Se ha exportado el inventario", Toast.LENGTH_SHORT).show();


            }
            fout.close();
        } catch (Exception ex) {
            Toast.makeText(context, "Error al escribir fichero", Toast.LENGTH_SHORT).show();

            Log.e("Ficheros", ex.getMessage());
        }

    }


    public static File leerFichero(String nombreFichero) throws IOException {
        File ruta = getRuta();
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero);
        return fichero;
    }

    public static File getRuta() {

        // El fichero sera almacenado en un directorio dentro del directorio
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
        }

        return ruta;
    }

}
