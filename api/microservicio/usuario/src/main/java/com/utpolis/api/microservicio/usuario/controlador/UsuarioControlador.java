package com.utpolis.api.microservicio.usuario.controlador;

import com.utpolis.api.microservicio.usuario.servicio.UsuarioServicio;
import com.utpolis.modelo.dto.UsuarioDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/usuario")
@RestController
@RestControllerAdvice
@Validated
@RequiredArgsConstructor
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class.getName());

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody UsuarioDto usuario) {
        try {
            return ResponseEntity.status(201).body(usuarioServicio.crear(usuario));
        } catch (Exception e) {
            logger.error("Error al crear usuario", e);
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        try {
            return ResponseEntity.status(200).body(usuarioServicio.obtener(id));
        } catch (Exception e) {
            logger.error("Error al obtener usuario", e);
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }

    @GetMapping
    public ResponseEntity<?> listar(@RequestParam(required = false) Integer numero, @RequestParam(required = false) Integer tamanio) {
        try {
            if (numero != null && tamanio != null)
                return ResponseEntity.status(200).body(usuarioServicio.paginar(PageRequest.of(numero, tamanio)));
            return ResponseEntity.status(200).body(usuarioServicio.paginar());
        } catch (Exception e) {
            logger.error("Error al listar usuarios", e);
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> error(Exception excepcion) { return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", excepcion.getMessage()))); }

}
