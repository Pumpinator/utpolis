package com.utpolis.api.microservicio.usuario.controlador;

import com.utpolis.api.microservicio.usuario.servicio.UsuarioServicio;
import com.utpolis.modelo.dto.UsuarioDto;
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

@RequestMapping("/usuario") // localhost:8081/usuario
@RestController
@RestControllerAdvice
@RequiredArgsConstructor
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class.getName());

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> crear(@RequestBody UsuarioDto usuario) {
        try {
            usuarioServicio.validar(usuario);
            return ResponseEntity.status(201).body(usuarioServicio.crear(usuario));
        } catch (Exception e) {
            logger.error("Error al crear usuario", e);
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMINISTRADOR') or principal == #id")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        try {
            return ResponseEntity.status(200).body(usuarioServicio.obtener(id));
        } catch (Exception e) {
            logger.error("Error al obtener usuario", e);
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> listar(@RequestParam(required = false) Integer numero, @RequestParam(required = false) Integer tamanio) {
        try {
            if (numero == null || tamanio == null) return ResponseEntity.status(200).body(usuarioServicio.listar());
            Page<UsuarioDto> pagina = usuarioServicio.paginar(PageRequest.of(numero - 1, tamanio));
            HashMap<String, Object> res = new HashMap<>(Map.of(
                    "usuarios", pagina.getContent(),
                    "total", pagina.getTotalElements(),
                    "pagina", pagina.getNumber() + 1,
                    "tamanio", pagina.getSize())
            );
            return ResponseEntity.status(200).body(res);
        } catch (Exception e) {
            logger.error("Error al listar usuarios", e);
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> modificar(@RequestBody UsuarioDto usuario) {
        try {
            usuarioServicio.validar(usuario);
            return ResponseEntity.status(200).body(usuarioServicio.modificar(usuario));
        } catch (Exception e) {
            logger.error("Error al modificar usuario", e);
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            return ResponseEntity.status(200).body(usuarioServicio.eliminar(id));
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
