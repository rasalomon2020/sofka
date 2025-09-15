package com.sofka.prueba.clienteservice.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "cliente")
public class Cliente extends Persona {

    private Long clienteId;
    private String contrasena;
    private String estado;

    public Cliente(Long clienteId, String contrasena, String estado) {
        super();
        this.clienteId = clienteId;
        this.contrasena = contrasena;
        this.estado = estado;
    }

    public Cliente() {

    }

}
