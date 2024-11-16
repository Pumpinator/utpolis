package com.utpolis.api.microservicio.persona.controlador;

import com.utpolis.api.microservicio.persona.repositorio.PersonaRepositorio;
import com.utpolis.api.microservicio.persona.servicio.PersonaServicio;
import com.utpolis.modelo.dto.PersonaDto;
import com.utpolis.modelo.entidad.Persona;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/persona")
@RestController
@RequiredArgsConstructor
public class PersonaControlador {

    private final PersonaServicio personaServicio;
    private static final Logger logger = LoggerFactory.getLogger(PersonaControlador.class.getName());
    private final PersonaRepositorio personaRepositorio;


    @PostMapping
    public ResponseEntity<?> crear(@RequestBody PersonaDto persona){

        try{
            return ResponseEntity.status(200).body(personaServicio.crear(persona));

        }catch(Exception e){
            logger.error("Error al crear persona", e);
        }
        return ResponseEntity.status(200).body("Persona creada");
    }

    @GetMapping
    public ResponseEntity<?> listar(){
        return ResponseEntity.status(200).body(personaServicio.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id){
        return ResponseEntity.status(200).body(personaServicio.obtener(id));
    }

}


