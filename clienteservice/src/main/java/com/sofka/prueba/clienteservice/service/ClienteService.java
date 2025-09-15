package com.sofka.prueba.clienteservice.service;

import com.sofka.prueba.clienteservice.model.Cliente;
import com.sofka.prueba.clienteservice.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes() {
        return  clienteRepository.findAll();
    }

    public Cliente crearCliente(Cliente cliente){
        clienteRepository.save(cliente);
        return cliente;
    }
}
