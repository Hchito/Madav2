package com.madara.mada.Entidades;

public class CalificadorData {
    private String apellido, comentario, nombre;
    double calificacion;

    public CalificadorData(String apellido, double calificacion, String comentario, String nombre){
        this.apellido = apellido;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.nombre = nombre;
    }

    public CalificadorData(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String toString(){return nombre + "\n" + apellido + "\n" + calificacion + "\n" + comentario;}
}
