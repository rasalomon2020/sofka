package com.sofka.prueba.cuenta_movimiento.service;

import com.sofka.prueba.cuenta_movimiento.model.Cuenta;
import com.sofka.prueba.cuenta_movimiento.repository.CuentaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    public CuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public List<Cuenta> listarCuntas() {
        return cuentaRepository.findAll();
    }

    public Optional<Cuenta> obtenerCuentaPorId(Long id) {
        return cuentaRepository.findById(id);
    }

    public ResponseEntity<Object> crearCuenta(Cuenta cuenta) {
        Cuenta cuenta1 = cuentaRepository.save(cuenta);

        return new ResponseEntity<>(cuenta1, HttpStatus.CREATED);
    }

    public Cuenta actualizarCuenta(Cuenta cuenta) {
        return cuentaRepository.saveAndFlush(cuenta);
    }

    public void eliminarCuenta(Long id) {
        cuentaRepository.deleteById(id);
    }


}
