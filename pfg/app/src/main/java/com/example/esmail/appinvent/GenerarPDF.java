package com.example.esmail.appinvent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import harmony.java.awt.Color;

public class GenerarPDF {

    private final static String NOMBRE_DIRECTORIO = "MiPdf";
    private final static String NOMBRE_DOCUMENTO = "inventario.pdf";
    private final static String ETIQUETA_ERROR = "ERROR";
    private GestionBBDD gestion;
    private Context context;

    public GenerarPDF(GestionBBDD gestion, Context context) {
        this.gestion = gestion;
        this.context = context;
    }

    public void actionPDFCreator() {

        // Creamos el documento.
        Document documento = new Document();

        try {

            // Creamos el fichero con el nombre que deseemos.
            File f = crearFichero(NOMBRE_DOCUMENTO);

            // Creamos el flujo de datos de salida para el fichero donde
            // guardaremos el pdf.
            FileOutputStream ficheroPdf = new FileOutputStream(
                    f.getAbsolutePath());

            // Asociamos el flujo que acabamos de crear al documento.
            PdfWriter writer = PdfWriter.getInstance(documento, ficheroPdf);

            // Incluimos el p?e de p?gina y una cabecera
            HeaderFooter cabecera = new HeaderFooter(new Phrase(
                    "REPORTE INVENTARIO"), false);

            HeaderFooter pie = new HeaderFooter(new Phrase(
                    "Página "), true);

            //documento.setHeader(cabecera);
            documento.setFooter(pie);
            // Abrimos el documento.
            documento.open();

            // A?adimos un t?tulo con la fuente por defecto.
            //documento.add(new Paragraph("T?tulo 1"));

            // A?adimos un t?tulo con una fuente personalizada.
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 25,
                    Font.BOLD, Color.RED);
            Paragraph p1 = new Paragraph("REPORTE INVENTARIO", font);
            p1.setAlignment(Element.ALIGN_CENTER);
            documento.add(p1);

            //Obtenemos la base de datos
            ArrayList<Row> arrayList = gestion.verDB();
            String[] cabeceraTabla = {BBDD.COLUMN_NAME_1, BBDD.COLUMN_NAME_2, BBDD.COLUMN_NAME_3, BBDD.COLUMN_NAME_4, BBDD.COLUMN_NAME_5, BBDD.COLUMN_NAME_6};
            ArrayList<String> al = new ArrayList<>();

            //Insertamos la cabecera
            for (int i = 0; i < cabeceraTabla.length; i++) {
                al.add(cabeceraTabla[i]);
            }
            //Insertamos los datos
            for (int i = 0; i < arrayList.size(); i++) {
                al.add(arrayList.get(i).getCodigo());
                al.add(arrayList.get(i).getCBarras());
                al.add(arrayList.get(i).getDescripcion());
                al.add(arrayList.get(i).getUnid());
                al.add(arrayList.get(i).getPrecio());
                al.add(arrayList.get(i).getImporte());
            }


            // Insertamos una tabla con los datos obtenidos de la base de datos
            PdfPTable tabla = new PdfPTable(cabeceraTabla.length);

            for (int i = 0; i < al.size(); i++) {
                tabla.addCell(al.get(i));
            }
            documento.add(tabla);
            //abrirPdf();


        } catch (DocumentException e) {

            Log.e(ETIQUETA_ERROR, e.getMessage());

        } catch (IOException e) {

            Log.e(ETIQUETA_ERROR, e.getMessage());

        } finally {
            Toast.makeText(context, "El pdf ha sido creado", Toast.LENGTH_SHORT).show();
            // Cerramos el documento.
            documento.close();
            abrirPdf();
        }
    }

    /**
     * Crea un fichero con el nombre que se le pasa a la funcion y en la ruta
     * especificada.
     *
     * @param nombreFichero
     * @return
     * @throws IOException
     */
    public static File crearFichero(String nombreFichero) throws IOException {
        File ruta = getRuta();
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero);
        return fichero;
    }

    /**
     * Obtenemos la ruta donde vamos a almacenar el fichero.
     *
     * @return
     */
    public static File getRuta() {

        // El fichero sera almacenado en un directorio dentro del directorio
        // Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            ruta = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    NOMBRE_DIRECTORIO);

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

    /**
     * Abre el pdf mediante una aplicacion externa
     */
    public Intent abrirPdf() {
        String filepath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                        "/" + NOMBRE_DIRECTORIO +
                        "/" + NOMBRE_DOCUMENTO;
        filepath = "content://com.estrongs.files/" + filepath;
        Uri uri;
        //Validación de acuerdo al OS.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = Uri.parse(filepath);
        } else {
            uri = Uri.fromFile(new File(filepath));
        }
        System.out.println(uri);

        Intent target = new Intent();
        target.setAction(Intent.ACTION_VIEW)
                .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                .setDataAndType(uri, "application/pdf");


        return target;
    }

}
