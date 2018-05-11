package com.example.esmail.appinvent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Clase que gestiona la base de datos
 */
public class GestionBBDD {

    private BBDD_Helper baseDatos;
    private SQLiteDatabase db;



    public GestionBBDD(Context context) {
        baseDatos = new BBDD_Helper(context);
    }

    /**
     * Insertar los datos en la base de datos
     *
     * @param codigo
     * @param cBarras
     * @param descripcion
     * @param unid
     * @param precio
     * @param importe
     * @return
     */
    public boolean insertar(String codigo, String cBarras, String descripcion, String unid, String precio, String importe) {
        try {

            db = baseDatos.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(BBDD.COLUMN_NAME_1, codigo);
            values.put(BBDD.COLUMN_NAME_2, cBarras);
            values.put(BBDD.COLUMN_NAME_3, descripcion);
            values.put(BBDD.COLUMN_NAME_4, unid);
            values.put(BBDD.COLUMN_NAME_5, precio);
            values.put(BBDD.COLUMN_NAME_6, importe);

            long newRowId = db.insert(BBDD.TABLE_NAME, null, values);
            return true;
        } catch (Exception e) {
            Log.e("DB", e.getMessage());
        } finally {
            db.close();

        }
        return false;
    }

    /**
     * Actualiza los datos de la base de datos
     *
     * @param columna
     * @param values
     * @param filtro
     * @return
     */
    public boolean actualizar(String columna, ContentValues values, String filtro) {
        try {
            db = baseDatos.getReadableDatabase();


            String selection = columna + " LIKE ?";
            String[] selectionArgs = {filtro};
            int count = db.update(
                    BBDD.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            return true;

        } catch (Exception e) {
            Log.e("DB", e.getMessage());
        } finally {
            db.close();

        }
        return false;
    }

    /**
     * Te devuelve un dato SOLOOO!!!
     *
     * @param columna
     * @param filtro
     * @return
     */
    public boolean consultar(String columna, String filtro) {
        try {
            db = baseDatos.getReadableDatabase();
            String valor = "";
            // Columnas
            String[] projection = {
                    columna

            };

            // Filter results WHERE "title" = 'My Title'
            String selection = columna + " = ?";
            String[] selectionArgs = {filtro};

            // How you want the results sorted in the resulting Cursor
            String sortOrder =
                    BBDD.COLUMN_NAME_1 + " ASC";

            Cursor c = db.query(
                    BBDD.TABLE_NAME,                            // tabla
                    projection,                                 // columnas
                    selection,                              // columnas para la clausula WHERE
                    selectionArgs,                           // valores para la clausula WHERE
                    null,
                    null,
                    sortOrder                                   // The sort order
            );
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    valor = c.getString(0);

                } while (c.moveToNext());
            }

            if (filtro.length() == valor.length()) {
                return true;
            } else return false;


        } catch (SQLException e) {
            Log.e("SQL", e.getMessage().toString());
        } finally {
            //cerramos la db
            db.close();
        }
        return false;
    }

    /**
     * Muestra la base de datos completa
     *
     * @return
     */
    public ArrayList<Row> verDB() {
        try {
            db = baseDatos.getReadableDatabase();

            // Columnas
            String[] projection = {
                    BBDD.COLUMN_NAME_1,
                    BBDD.COLUMN_NAME_2,
                    BBDD.COLUMN_NAME_3,
                    BBDD.COLUMN_NAME_4,
                    BBDD.COLUMN_NAME_5,
                    BBDD.COLUMN_NAME_6

            };

            // Filter results WHERE "title" = 'My Title'
            //String selection = BBDD.COLUMN_NAME_TITLE + " = ?";
            //String[] selectionArgs = { "My Title" };

            // How you want the results sorted in the resulting Cursor
            String sortOrder =
                    BBDD._ID + " ASC";

            Cursor c = db.query(
                    BBDD.TABLE_NAME,                            // tabla
                    projection,                                 // columnas
                    null,                              // columnas para la clausula WHERE
                    null,                           // valores para la clausula WHERE
                    null,
                    null,
                    sortOrder                                   // The sort order
            );
            ArrayList<Row> mostrar = new ArrayList<>();

            //Mostrar elementos desde la base de datos
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String codigo = c.getString(0);
                    String cBarras = c.getString(1);
                    String descripcion = c.getString(2);
                    String uni = c.getString(3);
                    String precio = c.getString(4);
                    String importe = c.getString(5);

                    mostrar.add(new Row(codigo, cBarras, descripcion, uni, precio, importe));
                } while (c.moveToNext());
            }
            return mostrar;


        } catch (SQLException e) {
            Log.e("SQL", e.getMessage().toString());
        } finally {
            //cerramos la db
            db.close();
        }

        return null;
    }

    /**
     * Borra los elementos de la tabla
     *
     * @return
     */
    public boolean eliminarBD() {
        db = baseDatos.getWritableDatabase();
        db.delete(BBDD.TABLE_NAME, null, null);
        db.close();
        return true;
    }

    /**
     * Elimina un solo registro
     * @param columna
     * @param filtro
     * @return
     */
    public boolean eliminarUnRegistro(String columna, String filtro) {
        db = baseDatos.getWritableDatabase();
        String selection = columna + " LIKE ?";
        String[] selectionArgs = {filtro};
        db.delete(BBDD.TABLE_NAME, selection, selectionArgs);
        db.close();
        return true;
    }
}
