package com.example.springjdbc;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Empleado {
    private int id;
    private String nombre;

    // Constructor vacío (necesario para deserialización)
    public Empleado() {}

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min =3, message = "El nombre debe tener al menos 3 caracteres")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
