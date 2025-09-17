package com.sofka.prueba.clienteservice.controller;

import com.sofka.prueba.clienteservice.model.Cliente;
import com.sofka.prueba.clienteservice.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1c")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping(path = "/clientes")
    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long id) {
        Cliente cliente = clienteService.obtenerClientePorId(id); // lanza excepción si no existe
        return ResponseEntity.ok(cliente);
    }

    @PostMapping(path = "/crear")
    public ResponseEntity<Object> crearCliente(@RequestBody Cliente cliente) {
        ResponseEntity<Object> clienteGuardado = clienteService.crearCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        if (cliente == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud no puede ser nulo.");
        }

        Cliente existente = clienteService.obtenerClientePorId(id); // lanza excepción si no existe

        existente.setContrasena(cliente.getContrasena());
        existente.setEstado(cliente.getEstado());
        existente.setNombre(cliente.getNombre());
        existente.setGenero(cliente.getGenero());
        existente.setEdad(cliente.getEdad());
        existente.setDireccion(cliente.getDireccion());
        existente.setIdentificacion(cliente.getIdentificacion());
        existente.setTelefono(cliente.getTelefono());

        Cliente actualizado = clienteService.actualizarCliente(existente); // nuevo método en el servicio

        return ResponseEntity.ok(actualizado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }

}

