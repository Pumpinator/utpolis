package com.utpolis.api.microservicio.usuario.controlador;

import com.utpolis.api.microservicio.usuario.servicio.ClienteServicio;
import com.utpolis.modelo.dto.UsuarioDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/cliente")
@RestController
@RestControllerAdvice
@RequiredArgsConstructor
public class ClienteControlador {

    private final ClienteServicio clienteServicio;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class.getName());

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody UsuarioDto usuario) {
        try {
            return ResponseEntity.status(201).body(clienteServicio.crear(usuario));
        } catch (Exception e) {
            logger.error("Error al crear usuario", e);
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        try {
            return ResponseEntity.status(200).body(clienteServicio.obtener(id));
        } catch (Exception e) {
            logger.error("Error al obtener usuario", e);
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }

    @GetMapping
    public ResponseEntity<?> listar(@RequestParam(required = false) Integer numero, @RequestParam(required = false) Integer tamanio) {
        try {
            if (numero == null || tamanio == null) return ResponseEntity.status(200).body(clienteServicio.listar());
            return ResponseEntity.status(200).body(clienteServicio.paginar(PageRequest.of(numero - 1, tamanio)));
        } catch (Exception e) {
            logger.error("Error al listar usuarios", e);
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }

    @PutMapping
    public ResponseEntity<?> modificar(@RequestBody UsuarioDto usuario) {
        try {
            return ResponseEntity.status(200).body(clienteServicio.modificar(usuario));
        } catch (Exception e) {
            logger.error("Error al modificar usuario", e);
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            return ResponseEntity.status(200).body(clienteServicio.eliminar(id));
        } catch (Exception e) {
            logger.error("Error al eliminar usuario", e);
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> error(Exception excepcion) {
        return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", excepcion.getMessage())));
    }

}