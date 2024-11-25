package com.utpolis.api.microservicio.persona.controlador;

import com.utpolis.api.microservicio.persona.repositorio.PersonaRepositorio;
import com.utpolis.api.microservicio.persona.servicio.PersonaServicio;
import com.utpolis.modelo.dto.PersonaDto;
import com.utpolis.modelo.dto.UsuarioDto;
import com.utpolis.modelo.entidad.Persona;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/persona")
@RestController
@RequiredArgsConstructor
public class PersonaControlador {

    private final PersonaServicio personaServicio;
    private static final Logger logger = LoggerFactory.getLogger(PersonaControlador.class.getName());

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<?> crear(@RequestBody PersonaDto persona) {
        try {
            return ResponseEntity.status(200).body(personaServicio.crear(persona));

        } catch (Exception e) {
            logger.error("Error al crear persona", e);
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EMPLEADO') or principal == #id")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        try {
            return ResponseEntity.status(200).body(personaServicio.obtener(id));
        } catch (Exception e) {
            logger.error("Error al obtener persona", e);
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EMPLEADO')")
    public ResponseEntity<?> listar(@RequestParam(required = false) Integer numero, @RequestParam(required = false) Integer tamanio) {
        try {
            if (numero == null || tamanio == null) return ResponseEntity.status(200).body(personaServicio.listar());
            Page<PersonaDto> pagina = personaServicio.paginar(PageRequest.of(numero - 1, tamanio));
            HashMap<String, Object> res = new HashMap<>(Map.of(
                    "personas", pagina.getContent(),
                    "total", pagina.getTotalElements(),
                    "pagina", pagina.getNumber() + 1,
                    "tamanio", pagina.getSize())
            );
            return ResponseEntity.status(200).body(res);
        } catch (Exception e) {
            logger.error("Error al listar personas", e);
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyRole('ADMINISTRADOR') or principal == #persona.id")
    public ResponseEntity<?> modificar(@RequestBody PersonaDto persona) {
        try {
            return ResponseEntity.status(200).body(personaServicio.modificar(persona));
        } catch (Exception e) {
            logger.error("Error al modificar usuario", e);
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }

}


