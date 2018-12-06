package com.madara.mada.Entidades;

public class Calificador extends CalificadorData{
    private String Id;

    public Calificador(String Id, String apellido, double calificacion, String comentarios, String nombre){
        super(apellido, calificacion, comentarios, nombre);
        this.Id = Id;
    }

    public Calificador(String Id, CalificadorData calificadorData){
        this.Id = Id;
        this.setApellido(calificadorData.getApellido());
        this.setNombre(calificadorData.getNombre());
        this.setCalificacion(calificadorData.getCalificacion());
        this.setComentario(calificadorData.getComentario());
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @Override
    public String toString(){
        return getId() + "\n"
                + getNombre() + "\n"
                + getApellido() + "\n"
                + getCalificacion() + "\n"
                + getComentario();
    }
}
