package com.madara.mada.Entidades.PaqEntidades;

import com.madara.mada.Entidades.DataUser;

public class Perfil extends DataUser {
    private String Id;

    public Perfil(String Id, String apellido, String direccion, String nombre, String telefono, String tipo){
        super(apellido, direccion, nombre, telefono, tipo);
        this.Id = Id;
    }

    public Perfil(){

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
