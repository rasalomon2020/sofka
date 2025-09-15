package com.sofka.prueba.clienteservice.service;

import com.sofka.prueba.clienteservice.model.Cliente;
import com.sofka.prueba.clienteservice.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes() {
        return  clienteRepository.findAll();
    }

    public ResponseEntity<Object> crearCliente(Cliente cliente){
        Optional<Cliente> cliente1 = null;
        HashMap<String, Object> datosCliente = new HashMap<>();

        if(cliente.getClienteId() != null) {
            cliente1 = clienteRepository.obtenerClientePorId(cliente.getClienteId());
        } else {
            datosCliente.put("error", true);
            datosCliente.put("mensaje", "El cliente debe tener un identificador.");
            return new ResponseEntity<>(datosCliente, HttpStatus.CONFLICT);
        }

        if(cliente1.isPresent()) {
            datosCliente.put("error", true);
            datosCliente.put("mensaje", "Ya existe el cliente.");
            return new ResponseEntity<>(datosCliente, HttpStatus.CONFLICT);
        }
        clienteRepository.save(cliente);
        datosCliente.put("datos", cliente);
        datosCliente.put("mensaje", "Se guardo con éxito");
        return new ResponseEntity<>(datosCliente, HttpStatus.CREATED);
    }

    public Optional<Cliente> obtenerClientePorId(Long id){
        return clienteRepository.findById(id);
    }

    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}
