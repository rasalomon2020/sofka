package com.sofka.prueba.cuenta_movimiento.repository;

import com.sofka.prueba.cuenta_movimiento.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    @Query("SELECT c FROM Cuenta c WHERE c.numeroCuenta = :numeroCuenta")
    public boolean obtenerCuentaPorId(@Param("numeroCuenta") String numeroCuenta);
}
