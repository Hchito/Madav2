package com.madara.mada.Entidades;

public class DataUser {
    private String apellido, direccion, nombre, telefono, tipo;

    public DataUser(String apellido, String direccion, String nombre, String telefono, String tipo){
        this.apellido = apellido;
        this.direccion = direccion;
        this.nombre = nombre;
        this.telefono = telefono;
        this.tipo = tipo;
    }

    public DataUser(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public String toString(){
        return "nombre: " + nombre + "\n"
                + "apellido: " + apellido + "\n"
                + "direccion: " + direccion + "\n"
                + "telefono: " + telefono + "\n"
                + "tipo: " + tipo + "\n";

    }
}
