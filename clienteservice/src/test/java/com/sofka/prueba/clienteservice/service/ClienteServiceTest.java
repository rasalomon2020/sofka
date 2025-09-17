package com.sofka.prueba.clienteservice.service;

import com.sofka.prueba.clienteservice.dto.ClienteDto;
import com.sofka.prueba.clienteservice.mapper.ClienteMapper;
import com.sofka.prueba.clienteservice.model.Cliente;
import com.sofka.prueba.clienteservice.model.Persona;
import com.sofka.prueba.clienteservice.repository.ClienteRepository;
import com.sofka.prueba.clienteservice.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private ClienteDto clienteDto;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setClienteId(1L);
        cliente.setNombre("René");
        cliente.setGenero("Masculino");
        cliente.setEdad(30);
        cliente.setIdentificacion("12345678");
        cliente.setDireccion("Montevideo");
        cliente.setTelefono("099123456");
        cliente.setContrasena("secreta");
        cliente.setEstado("ACTIVO");

        clienteDto = new ClienteDto();
        clienteDto.setNombre("René");
        clienteDto.setGenero("Masculino");
        clienteDto.setEdad(30);
        clienteDto.setIdentificacion("12345678");
        clienteDto.setDireccion("Montevideo");
        clienteDto.setTelefono("099123456");
        clienteDto.setContrasena("secreta");
        clienteDto.setEstado("ACTIVO");
    }

    @Test
    void listarClientes_debeRetornarLista() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        List<Cliente> resultado = clienteService.listarClientes();

        assertEquals(1, resultado.size());
        assertEquals("René", resultado.get(0).getNombre());
    }

    @Test
    void listarClientes_sinClientes_debeLanzarExcepcion() {
        when(clienteRepository.findAll()).thenReturn(List.of());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> clienteService.listarClientes());
        assertEquals(HttpStatus.NO_CONTENT, ex.getStatusCode());
    }

    @Test
    void crearCliente_valido_debeGuardarYEnviarMensaje() {
        when(clienteRepository.obtenerClientePorId(1L)).thenReturn(Optional.empty());
        when(personaRepository.obtenerIdentificacion("12345678")).thenReturn(Optional.empty());
        when(clienteMapper.toDto(cliente)).thenReturn(clienteDto);

        ResponseEntity<Object> response = clienteService.crearCliente(cliente);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(clienteRepository).save(cliente);
        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), eq(clienteDto));
    }

    @Test
    void crearCliente_conIdNulo_debeRetornarConflict() {
        cliente.setClienteId(null);

        ResponseEntity<Object> response = clienteService.crearCliente(cliente);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(((HashMap<?, ?>) response.getBody()).containsKey("mensaje"));
    }

    @Test
    void crearCliente_existentePorId_debeRetornarConflict() {
        when(clienteRepository.obtenerClientePorId(1L)).thenReturn(Optional.of(cliente));

        ResponseEntity<Object> response = clienteService.crearCliente(cliente);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void crearCliente_existentePorIdentificacion_debeRetornarConflict() {
        when(clienteRepository.obtenerClientePorId(1L)).thenReturn(Optional.empty());
        when(personaRepository.obtenerIdentificacion("12345678")).thenReturn(Optional.of(new Persona()));

        ResponseEntity<Object> response = clienteService.crearCliente(cliente);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

}