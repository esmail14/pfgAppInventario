package com.example.esmail.appinvent;

/**
 * Clase Row. Representa una fila de la base de datos
 */
public class Row {
    private String Codigo, CBarras, Descripcion, Unid, Precio, Importe;

    public Row(String codigo, String CBarras, String descripcion, String unid, String precio, String importe) {
        Codigo = codigo;
        this.CBarras = CBarras;
        Descripcion = descripcion;
        Unid = unid;
        Precio = precio;
        Importe = importe;
    }

    public String getCodigo() {
        return Codigo;
    }

    public String getCBarras() {
        return CBarras;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public String getUnid() {
        return Unid;
    }

    public String getPrecio() {
        return Precio;
    }

    public String getImporte() {
        return Importe;
    }
}
