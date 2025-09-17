package com.sofka.prueba.clienteservice.repository;

import com.sofka.prueba.clienteservice.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    @Query("SELECT p FROM Persona p WHERE p.identificacion = :identificacion")
    Optional<Persona> obtenerIdentificacion(@Param("identificacion") String identificacion);
}
