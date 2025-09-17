package com.sofka.prueba.cuenta_movimiento.service;

import com.sofka.prueba.cuenta_movimiento.model.Cuenta;
import com.sofka.prueba.cuenta_movimiento.repository.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private CuentaService cuentaService;

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta();
        cuenta.setCuentaId(1L);
        cuenta.setNumeroCuenta("123456");
        cuenta.setTipoCuenta("Ahorro");
        cuenta.setSaldoInicial(BigDecimal.valueOf(1000).doubleValue());
        cuenta.setEstado(true);
    }

    @Test
    void listarCuentas_debeRetornarListaDeCuentas() {
        when(cuentaRepository.findAll()).thenReturn(List.of(cuenta));

        List<Cuenta> resultado = cuentaService.listarCuentas();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(cuentaRepository).findAll();
    }

    @Test
    void listarCuentas_sinResultados_debeLanzarExcepcion() {
        when(cuentaRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> cuentaService.listarCuentas());
        assertEquals(HttpStatus.NO_CONTENT, ex.getStatusCode());
    }

    @Test
    void obtenerCuentaPorId_existente_debeRetornarCuenta() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        Optional<Cuenta> resultado = cuentaService.obtenerCuentaPorId(1L);

        assertEquals("123456", resultado.get().getNumeroCuenta());
        verify(cuentaRepository).findById(1L);
    }

    @Test
    void obtenerCuentaPorId_inexistente_debeLanzarExcepcion() {
        when(cuentaRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> cuentaService.obtenerCuentaPorId(99L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void crearCuenta_valida_debeGuardarYRetornarCuenta() {
        when(cuentaRepository.obtenerCuentaPorId("123456")).thenReturn(false);
        when(cuentaRepository.save(cuenta)).thenReturn(cuenta);

        ResponseEntity<Object> response = cuentaService.crearCuenta(cuenta);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(cuentaRepository).save(cuenta);
    }

    @Test
    void crearCuenta_conNumeroExistente_debeLanzarExcepcion() {
        when(cuentaRepository.obtenerCuentaPorId("123456")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> cuentaService.crearCuenta(cuenta));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void actualizarCuenta_existente_debeActualizar() {
        when(cuentaRepository.existsById(1L)).thenReturn(true);
        when(cuentaRepository.saveAndFlush(cuenta)).thenReturn(cuenta);

        Cuenta actualizada = cuentaService.actualizarCuenta(cuenta);

        assertEquals("123456", actualizada.getNumeroCuenta());
        verify(cuentaRepository).saveAndFlush(cuenta);
    }

    @Test
    void actualizarCuenta_inexistente_debeLanzarExcepcion() {
        when(cuentaRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> cuentaService.actualizarCuenta(cuenta));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void eliminarCuenta_existente_debeEliminar() {
        when(cuentaRepository.existsById(1L)).thenReturn(true);

        cuentaService.eliminarCuenta(1L);

        verify(cuentaRepository).deleteById(1L);
    }

    @Test
    void eliminarCuenta_inexistente_debeLanzarExcepcion() {
        when(cuentaRepository.existsById(99L)).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> cuentaService.eliminarCuenta(99L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

}