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
    public List<Cliente> listarClientes(){
        return clienteService.listarClientes();
    }

    @PostMapping(path = "/crear")
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente){
        Cliente clienteGuardado = clienteService.crearCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);
    }
}

