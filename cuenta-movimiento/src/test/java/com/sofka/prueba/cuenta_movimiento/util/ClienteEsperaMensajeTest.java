package com.sofka.prueba.cuenta_movimiento.util;

import com.sofka.prueba.cuenta_movimiento.dto.ClienteDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClienteEsperaMensajeTest {

    private ClienteEsperaMensaje clienteEsperaMensaje;

    @BeforeEach
    void setUp() {
        clienteEsperaMensaje = new ClienteEsperaMensaje();
    }

    @Test
    void clienteValido_noDebeTenerErrores() {
        ClienteDto cliente = new ClienteDto();
        cliente.setPersonaId(1L);
        cliente.setNombre("René");
        cliente.setIdentificacion("12345678");
        cliente.setEdad(35);
        cliente.setEstado("ACTIVO");

        List<String> errores = clienteEsperaMensaje.validarCliente(cliente);

        assertTrue(errores.isEmpty(), "No debería haber errores para un cliente válido");
    }

    @Test
    void clienteNulo_debeRetornarError() {
        List<String> errores = clienteEsperaMensaje.validarCliente(null);

        assertEquals(1, errores.size());
        assertEquals("El cliente recibido es nulo.", errores.get(0));
    }

    @Test
    void clienteConCamposInvalidos_debeRetornarErrores() {
        ClienteDto cliente = new ClienteDto(); // todos los campos nulos o inválidos

        List<String> errores = clienteEsperaMensaje.validarCliente(cliente);

        assertTrue(errores.contains("personaId es obligatorio."));
        assertTrue(errores.contains("nombre es obligatorio."));
        assertTrue(errores.contains("identificación es obligatoria."));
        assertTrue(errores.contains("edad debe ser mayor a 0."));
        assertTrue(errores.contains("estado debe ser ACTIVO o INACTIVO."));
        assertEquals(5, errores.size());
    }

}