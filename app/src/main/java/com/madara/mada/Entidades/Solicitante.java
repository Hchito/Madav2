package com.madara.mada.Entidades;

public class Solicitante {
    private String Nombre, Apellido, Direccion, Telefono;
    public Solicitante(String Nombre, String Apellido, String Direccion, String Telefono){
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Direccion = Direccion;
        this.Telefono = Telefono;
    }

    public Solicitante() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

}
