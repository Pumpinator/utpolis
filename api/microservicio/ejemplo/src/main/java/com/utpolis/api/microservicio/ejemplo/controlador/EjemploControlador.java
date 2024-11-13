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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EjemploControlador { // Controlador de prueba para subir archivos y obtener información del usuario

    private final ArchivoServicio archivoServicio; // Servicio para subir archivos
    private final UsuarioServicio usuarioServicio; // Servicio para obtener información del usuario
    private static final Logger logger = LoggerFactory.getLogger(EjemploControlador.class.getName());

    @GetMapping("/probar") // Método para obtener información del usuario
    public ResponseEntity<?> test(@RequestHeader Map<String, String> headers) throws JsonProcessingException {
        String username = usuarioServicio.obtenerSujeto(headers.get("authorization"));
        UsuarioDto usuario = usuarioServicio.obtener(username);

        headers.put("username", usuario.getUsername());
        headers.put("correo", usuario.getCorreo());
        headers.put("rol", usuario.getRol());
        headers.put("id", usuario.getId().toString());

        String datos = new ObjectMapper().writeValueAsString(headers);

        logger.info(datos);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(datos);
    }

    @PostMapping("/subir") // Método para subir archivos
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile file) {
        try {
            archivoServicio.guardar(file);
            return ResponseEntity.status(201).body(new HashMap<>(Map.of("mensaje", "Archivo subido correctamente")));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }
}
