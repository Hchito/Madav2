package com.madara.mada;

public class usuario {
    public String Id;
    public String Nombre;
    public String Apellido;
    public String Direccion;
    public String Telefono;
    public String Fecha_de_nacimiento;
    public String Ocupacion;
    public String Email;

    public usuario() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public usuario(String userID, String Nombre, String Apellido, String Direccion, String Telefono, String Fecha, String Ocupacion,String Email) {
        this.Id=userID;
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Direccion=Direccion;
        this.Telefono=Telefono;
        this.Fecha_de_nacimiento = Fecha;
        this.Ocupacion = Ocupacion;
        this.Email=Email;

    }
    public String getEmail() {
        return Email;
    }
    public String getUsername() {
        return Nombre;
    }
    public void setEmail(String email) {
        this.Email = email;
    }
    public void setUsername(String username) {
        this.Nombre = username;
    }
}
