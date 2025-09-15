package com.sofka.prueba.clienteservice.controller;
import com.sofka.prueba.clienteservice.model.Persona;
import com.sofka.prueba.clienteservice.service.PersonaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path ="api/v1p")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping(path = "/obtener")
    public List<Persona> listarPersonas(){
        return personaService.listarPersonas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> buscarPorId(@PathVariable Long id) {
        return personaService.obtenerPersonaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/crear")
    public ResponseEntity<Persona> crear(@RequestBody Persona persona) {
        Persona guardada = personaService.altaPersona(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Persona> actualizar(@PathVariable Long id, @RequestBody Persona persona) {
        return personaService.obtenerPersonaPorId(id)
                .map(p -> {
                    p.setNombre(persona.getNombre());
                    p.setGenero(persona.getGenero());
                    p.setEdad(persona.getEdad());
                    p.setDireccion(persona.getDireccion());
                    p.setTelefono(persona.getTelefono());
                    p.setIdentificacion(persona.getIdentificacion());
                    Persona actualizado = personaService.altaPersona(p);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE: Eliminar persona por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (personaService.obtenerPersonaPorId(id).isPresent()) {
            personaService.eliminarPersona(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
