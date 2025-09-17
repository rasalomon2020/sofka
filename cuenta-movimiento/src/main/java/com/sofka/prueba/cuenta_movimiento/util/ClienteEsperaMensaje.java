package com.sofka.prueba.cuenta_movimiento.util;

import com.sofka.prueba.cuenta_movimiento.config.RabbitConfig;
import com.sofka.prueba.cuenta_movimiento.dto.ClienteDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClienteEsperaMensaje {

    @RabbitListener(queues = RabbitConfig.QUEUE_CLIENTE)
    public void recibirMensaje(ClienteDto cliente) {
        List<String> errores = validarCliente(cliente);

        if (!errores.isEmpty()) {
            System.err.println("Errores de validación en ClienteDto:");
            errores.forEach(System.err::println);
            return;
        }

        System.out.println("Cliente válido: " + cliente.getNombre());
    }

    public List<String> validarCliente(ClienteDto cliente) {
        List<String> errores = new ArrayList<>();

        if (cliente == null) {
            errores.add("El cliente recibido es nulo.");
        } else {
            if (cliente.getPersonaId() == null) errores.add("personaId es obligatorio.");
            if (cliente.getNombre() == null || cliente.getNombre().isBlank()) errores.add("nombre es obligatorio.");
            if (cliente.getIdentificacion() == null || cliente.getIdentificacion().isBlank())
                errores.add("identificación es obligatoria.");
            if (cliente.getEdad() <= 0) errores.add("edad debe ser mayor a 0.");
            if (cliente.getEstado() == null || (!cliente.getEstado().equalsIgnoreCase("ACTIVO") && !cliente.getEstado().equalsIgnoreCase("INACTIVO")))
                errores.add("estado debe ser ACTIVO o INACTIVO.");
        }

        return errores;
    }

}
