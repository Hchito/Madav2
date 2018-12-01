package com.madara.mada.Entidades;

public class Tipo_de_usuario {
    private String tipo;
    public Tipo_de_usuario(String tipo){
        tipo = tipo.replace("{", "");
        tipo = tipo.replace("}", "");
        tipo = tipo.replace("Tipo=", "");
        this.tipo = tipo;
    }

    public Tipo_de_usuario(){

    }

    public String gettipo() {
        return tipo;
    }

    public void settipo(String tipo) {
        tipo = tipo;
    }

    @Override

    public String toString() {
        return "{tipo='"+
                tipo+
                "'}";
    }
}
