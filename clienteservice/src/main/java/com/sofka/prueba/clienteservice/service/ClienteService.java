package com.sofka.prueba.clienteservice.service;

import com.sofka.prueba.clienteservice.config.RabbitConfig;
import com.sofka.prueba.clienteservice.dto.ClienteDto;
import com.sofka.prueba.clienteservice.mapper.ClienteMapper;
import com.sofka.prueba.clienteservice.model.Cliente;
import com.sofka.prueba.clienteservice.model.Persona;
import com.sofka.prueba.clienteservice.repository.ClienteRepository;
import com.sofka.prueba.clienteservice.repository.PersonaRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PersonaRepository personaRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ClienteMapper clienteMapper;


    public ClienteService(ClienteRepository clienteRepository, PersonaRepository personaRepository, RabbitTemplate rabbitTemplate, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.personaRepository = personaRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.clienteMapper = clienteMapper;
    }

    public List<Cliente> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        if (clientes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay clientes registrados.");
        }
        return clientes;
    }


    public ResponseEntity<Object> crearCliente(Cliente cliente) {
        Optional<Cliente> cliente1 = null;
        HashMap<String, Object> datosCliente = new HashMap<>();

        if (cliente.getClienteId() != null) {
            cliente1 = clienteRepository.obtenerClientePorId(cliente.getClienteId());
        } else {
            datosCliente.put("error", true);
            datosCliente.put("mensaje", "El cliente debe tener un identificador.");
            return new ResponseEntity<>(datosCliente, HttpStatus.CONFLICT);
        }

        if (cliente1.isPresent()) {
            datosCliente.put("error", true);
            datosCliente.put("mensaje", "Ya existe el cliente.");
            return new ResponseEntity<>(datosCliente, HttpStatus.CONFLICT);
        }

        if (cliente.getIdentificacion() != null) {
            Optional<Persona> persona = personaRepository.obtenerIdentificacion(cliente.getIdentificacion());
            if (persona.isPresent()) {
                datosCliente.put("error", true);
                datosCliente.put("mensaje", "Ya existe el cliente con la identificacion: " + cliente.getIdentificacion());
                return new ResponseEntity<>(datosCliente, HttpStatus.CONFLICT);
            }
        }
        clienteRepository.save(cliente);
        datosCliente.put("datos", cliente);
        datosCliente.put("mensaje", "Se guardo con éxito");

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_CLIENTE,
                RabbitConfig.ROUTING_KEY,
                obtenerDtoDesdeEntidad(cliente));

        return new ResponseEntity<>(datosCliente, HttpStatus.CREATED);
    }

    public Cliente actualizarCliente(Cliente cliente) {
        if (cliente == null || cliente.getClienteId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente o ID nulo.");
        }

        if (!clienteRepository.existsById(cliente.getClienteId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe cliente con ID: " + cliente.getClienteId());
        }

        return clienteRepository.saveAndFlush(cliente);
    }

    public Cliente obtenerClientePorId(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de cliente inválido.");
        }

        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con ID: " + id));
    }

    public void eliminarCliente(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID inválido para eliminar cliente.");
        }

        if (!clienteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un cliente con ID: " + id);
        }

        clienteRepository.deleteById(id);
    }


    public ClienteDto obtenerDtoDesdeEntidad(Cliente cliente) {
        if (cliente == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entidad Cliente nula.");
        }

        ClienteDto dto = clienteMapper.toDto(cliente);
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al convertir Cliente a DTO.");
        }

        return dto;
    }

    public Cliente convertirDesdeDto(ClienteDto dto) { //Todo: Por el momento no lo necesito. Revisar luego
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DTO de Cliente nulo.");
        }

        Cliente cliente = clienteMapper.toEntity(dto);
        if (cliente == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al convertir DTO a Cliente.");
        }

        return cliente;
    }


}
