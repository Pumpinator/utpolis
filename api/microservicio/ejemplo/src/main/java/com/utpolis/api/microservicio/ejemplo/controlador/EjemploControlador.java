package com.utpolis.api.microservicio.ejemplo.controlador;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utpolis.api.microservicio.ejemplo.servicio.ArchivoServicio;
import com.utpolis.api.microservicio.ejemplo.servicio.UsuarioServicio;
import com.utpolis.modelo.dto.UsuarioDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EjemploControlador { // Controlador de prueba para subir archivos y obtener información del usuario

    private final ArchivoServicio archivoServicio; // Servicio para subir archivos
    private final UsuarioServicio usuarioServicio; // Servicio para obtener información del usuario
    private static final Logger logger = LoggerFactory.getLogger(EjemploControlador.class.getName());

    @GetMapping("/yo") // Método para obtener información del usuario ingresado
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'EMPLEADO', 'CLIENTE')")
    public ResponseEntity<?> test(@RequestHeader Map<String, String> headers) throws JsonProcessingException {
        String username = usuarioServicio.obtenerSujeto(headers.get("authorization"));
        UsuarioDto usuario = usuarioServicio.obtener(username);

        headers.put("username", usuario.getUsername());
        headers.put("correo", usuario.getCorreo());
        headers.put("rol", usuario.getRol());
        headers.put("id", usuario.getId().toString());

        String res = new ObjectMapper().writeValueAsString(headers);

        logger.info(res);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(res);
    }

    @PostMapping("/subir") // Método para subir archivos
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile file) {
        try {
            archivoServicio.guardar(file);
            return ResponseEntity.status(201).body(new HashMap<>(Map.of("mensaje", "Archivo subido correctamente")));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }

    private String obtenerRol(Collection<? extends GrantedAuthority> authorities) {
        String rol = authorities.stream().findFirst().orElseThrow(
                () -> new IllegalArgumentException("No se encontró el rol del usuario")
        ).getAuthority().replace("ROLE_", "").replace("[", "").replace("]", "");
        return rol.charAt(0) + rol.substring(1).toLowerCase();
    }
}
