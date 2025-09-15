package com.sofka.prueba.clienteservice.repository;

import com.sofka.prueba.clienteservice.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
}
