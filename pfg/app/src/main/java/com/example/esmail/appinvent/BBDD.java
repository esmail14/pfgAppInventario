package com.example.esmail.appinvent;

import android.provider.BaseColumns;

public class BBDD implements BaseColumns {

    public static final String TABLE_NAME = "inventario";
    public static final String COLUMN_NAME_1 = "Codigo";
    public static final String COLUMN_NAME_2 = "CBarras";
    public static final String COLUMN_NAME_3 = "Descripcion";
    public static final String COLUMN_NAME_4 = "Unid";
    public static final String COLUMN_NAME_5 = "Precio";
    public static final String COLUMN_NAME_6 = "Importe";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BBDD.TABLE_NAME + " (" +
                    BBDD._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    BBDD.COLUMN_NAME_1 + " TEXT," +
                    BBDD.COLUMN_NAME_2 + " TEXT," +
                    BBDD.COLUMN_NAME_3 + " TEXT," +
                    BBDD.COLUMN_NAME_4 + " INTEGER," +
                    BBDD.COLUMN_NAME_5 + " INTEGER," +
                    BBDD.COLUMN_NAME_6 + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + BBDD.TABLE_NAME;
}

