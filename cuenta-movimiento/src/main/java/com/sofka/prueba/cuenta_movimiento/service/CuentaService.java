package com.sofka.prueba.cuenta_movimiento.service;

import com.sofka.prueba.cuenta_movimiento.model.Cuenta;
import com.sofka.prueba.cuenta_movimiento.repository.CuentaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    public CuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public List<Cuenta> listarCuentas() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        if (cuentas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay cuentas registradas.");
        }
        return cuentas;
    }

    public Optional<Cuenta> obtenerCuentaPorId(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID de cuenta es inválido.");
        }

        return Optional.ofNullable(cuentaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada con ID: " + id)));
    }

    public ResponseEntity<Object> crearCuenta(Cuenta cuenta) {
        if (cuenta == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cuenta no puede ser nula.");
        }

        if (cuenta.getNumeroCuenta() == null || cuenta.getNumeroCuenta().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de cuenta es obligatorio.");
        }

        if (cuentaRepository.obtenerCuentaPorId(cuenta.getNumeroCuenta())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una cuenta con ese número.");
        }

        if (cuenta.getSaldoInicial() == null || cuenta.getSaldoInicial().compareTo(BigDecimal.ZERO.doubleValue()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El saldo inicial no puede ser negativo.");
        }

        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        return new ResponseEntity<>(cuentaGuardada, HttpStatus.CREATED);
    }

    public Cuenta actualizarCuenta(Cuenta cuenta) {
        if (cuenta == null || cuenta.getCuentaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cuenta o su ID no pueden ser nulos.");
        }

        if (!cuentaRepository.existsById(cuenta.getCuentaId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe una cuenta con ID: " + cuenta.getCuentaId());
        }

        return cuentaRepository.saveAndFlush(cuenta);
    }


    public void eliminarCuenta(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID inválido para eliminar cuenta.");
        }

        if (!cuentaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe una cuenta con ID: " + id);
        }

        cuentaRepository.deleteById(id);
    }

}
