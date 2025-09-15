package com.sofka.prueba.cuenta_movimiento.service;

import com.sofka.prueba.cuenta_movimiento.exeption.SaldoInsuficienteException;
import com.sofka.prueba.cuenta_movimiento.model.Cuenta;
import com.sofka.prueba.cuenta_movimiento.model.Movimiento;
import com.sofka.prueba.cuenta_movimiento.repository.CuentaRepository;
import com.sofka.prueba.cuenta_movimiento.repository.MovimientoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@Service
public class MovimientoService {

    private final CuentaRepository cuentaRepository;

    private final MovimientoRepository movimientoRepository;

    public MovimientoService(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }


    public Movimiento registrarMovimiento(Movimiento movimiento) {
        Cuenta cuenta = cuentaRepository.findById(movimiento.getCuenta().getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        Double nuevoSaldo = calcularNuevoSaldo(cuenta.getSaldoInicial(), movimiento.getValor(), movimiento.getTipoMovimiento());

        if (nuevoSaldo < 0) {
            throw new SaldoInsuficienteException("Saldo no disponible");
        }

        cuenta.setSaldoInicial(nuevoSaldo);
        cuentaRepository.save(cuenta);

        movimiento.setSaldo(nuevoSaldo);
        movimiento.setFecha(LocalDate.now());
        return movimientoRepository.save(movimiento);
    }

    public Double calcularNuevoSaldo(Double saldoActual, Double valorMovimiento, String tipoMovimiento) {
        if (tipoMovimiento == null || saldoActual == null || valorMovimiento == null) {
            throw new IllegalArgumentException("Parámetros inválidos");
        }

        switch (tipoMovimiento.toUpperCase()) {
            case "DEPOSITO":
                return saldoActual + valorMovimiento;

            case "RETIRO":
                Double nuevoSaldo = saldoActual - valorMovimiento;
                if (nuevoSaldo < 0) {
                    throw new SaldoInsuficienteException("Saldo insuficiente para realizar el retiro");
                }
                return nuevoSaldo;

            default:
                throw new IllegalArgumentException("Tipo de movimiento no reconocido: " + tipoMovimiento);
        }
    }


}
