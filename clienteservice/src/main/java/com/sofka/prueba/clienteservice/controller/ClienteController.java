package com.sofka.prueba.clienteservice.controller;

import com.sofka.prueba.clienteservice.model.Cliente;
import com.sofka.prueba.clienteservice.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(path = "/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long id) {
        return clienteService.obtenerClientePorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/crear")
    public ResponseEntity<Object> crearCliente(@RequestBody Cliente cliente) {
        ResponseEntity<Object> clienteGuardado = clienteService.crearCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);
    }

    @PutMapping("/id")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        return clienteService.obtenerClientePorId(id).map(c -> {
            c.setContrasena(cliente.getContrasena());
            c.setEstado(cliente.getEstado());
            c.setNombre(cliente.getNombre());
            c.setGenero(cliente.getGenero());
            c.setEdad(cliente.getEdad());
            c.setDireccion(cliente.getDireccion());
            c.setIdentificacion(cliente.getIdentificacion());
            c.setTelefono(cliente.getTelefono());
            Cliente actualizado = (Cliente) clienteService.crearCliente(c).getBody();
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/id")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        if (clienteService.obtenerClientePorId(id).isPresent()) {
            clienteService.eliminarCliente(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}

