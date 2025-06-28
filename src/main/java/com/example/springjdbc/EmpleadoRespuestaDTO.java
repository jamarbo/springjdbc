package com.example.springjdbc;

import java.security.PublicKey;

public class EmpleadoRespuestaDTO {

    private String nombre;

    public EmpleadoRespuestaDTO() {}

    public EmpleadoRespuestaDTO(String nombre){
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
