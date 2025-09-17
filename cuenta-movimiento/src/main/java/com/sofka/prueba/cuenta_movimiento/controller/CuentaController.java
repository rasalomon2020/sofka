package com.sofka.prueba.cuenta_movimiento.controller;

import com.sofka.prueba.cuenta_movimiento.model.Cuenta;
import com.sofka.prueba.cuenta_movimiento.service.CuentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService){
        this.cuentaService = cuentaService;
    }

    @GetMapping(path = "/cuentas")
    public List<Cuenta> listarCuentas() {
       return cuentaService.listarCuentas();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Cuenta> obtenerClientePorId(@PathVariable Long id) {
        return cuentaService.obtenerCuentaPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/crear")
    public ResponseEntity<Object> crearCuenta(@RequestBody Cuenta cuenta) {
        ResponseEntity<Object> clienteGuardado = cuentaService.crearCuenta(cuenta);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteGuardado);
    }

    @PutMapping("/id")
    public ResponseEntity<Cuenta> antualizarCuenta(@PathVariable Long id, @RequestBody Cuenta cuenta){
        return cuentaService.obtenerCuentaPorId(id).map(c -> {
            c.setNumeroCuenta(cuenta.getNumeroCuenta());
            c.setTipoCuenta(cuenta.getTipoCuenta());
            c.setSaldoInicial(cuenta.getSaldoInicial());
            c.setEstado(cuenta.getEstado());
            Cuenta actualizado = (Cuenta) cuentaService.actualizarCuenta(c);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/id")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        if (cuentaService.obtenerCuentaPorId(id).isPresent()) {
            cuentaService.eliminarCuenta(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }




}
