package com.sofka.prueba.cuenta_movimiento.controller;

import com.sofka.prueba.cuenta_movimiento.model.Movimiento;
import com.sofka.prueba.cuenta_movimiento.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping(path = "/crear")
    public ResponseEntity<Movimiento> registrarMovimiento(@RequestBody Movimiento movimiento) {
        Movimiento registrado = movimientoService.registrarMovimiento(movimiento);
        return ResponseEntity.ok(registrado);
    }

}
