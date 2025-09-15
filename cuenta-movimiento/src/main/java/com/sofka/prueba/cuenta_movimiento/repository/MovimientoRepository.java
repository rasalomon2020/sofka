package com.sofka.prueba.cuenta_movimiento.repository;

import com.sofka.prueba.cuenta_movimiento.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
}
