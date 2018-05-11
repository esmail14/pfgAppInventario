package com.example.esmail.appinvent;

public class Item {
    private String etiqueta;
    private String texto;

    public Item(String etiqueta, String texto) {
        this.etiqueta = etiqueta;
        this.texto = texto;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
