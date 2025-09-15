package com.sofka.prueba.cuenta_movimiento.repository;

import com.sofka.prueba.cuenta_movimiento.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
}
