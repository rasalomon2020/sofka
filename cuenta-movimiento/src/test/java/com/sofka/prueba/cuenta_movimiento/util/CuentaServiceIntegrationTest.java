package com.sofka.prueba.cuenta_movimiento.util;

import com.sofka.prueba.cuenta_movimiento.model.Cuenta;
import com.sofka.prueba.cuenta_movimiento.repository.CuentaRepository;
import com.sofka.prueba.cuenta_movimiento.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class CuentaServiceIntegrationTest {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CuentaRepository cuentaRepository;

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta();
        cuenta.setNumeroCuenta("987654");
        cuenta.setTipoCuenta("Corriente");
        cuenta.setSaldoInicial(BigDecimal.valueOf(5000).doubleValue());
        cuenta.setEstado(true);
    }

    private void assertEquals(String number, String numeroCuenta) {
    }


    @Test
    void listarCuentas_debeRetornarLista() {
        cuentaRepository.save(cuenta);

        List<Cuenta> cuentas = cuentaService.listarCuentas();

        assertFalse(cuentas.isEmpty());
    }

    @Test
    void eliminarCuenta_debeEliminarCuenta() {
        Cuenta guardada = cuentaRepository.save(cuenta);

        cuentaService.eliminarCuenta(guardada.getCuentaId());

        assertFalse(cuentaRepository.existsById(guardada.getCuentaId()));
    }



}

