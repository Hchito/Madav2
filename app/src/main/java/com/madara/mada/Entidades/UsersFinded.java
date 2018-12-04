package com.madara.mada.Entidades;

public class UsersFinded {
    private String Nombre, Apellido;
    double calificacion;
    public UsersFinded(String Nombre, String Apellido){
        this.Nombre = Nombre;
        this.Apellido = Apellido;
    }

    public UsersFinded(){

    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        calificacion = calificacion;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
