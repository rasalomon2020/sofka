package com.sofka.prueba.clienteservice.repository;

import com.sofka.prueba.clienteservice.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT c FROM Cliente c WHERE c.clienteId = :clienteId")
    Optional<Cliente> obtenerClientePorId(@Param("clienteId") Long clienteId);

}
