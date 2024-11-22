package com.utpolis.api.microservicio.boleto.controlador;

import com.utpolis.api.microservicio.boleto.servicio.VentaServicio;
import com.utpolis.modelo.dto.VentaDto;
import com.utpolis.modelo.entidad.Roles;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/venta")
@RestController
@RestControllerAdvice
@RequiredArgsConstructor
public class VentaControlador {

    private final VentaServicio ventaServicio;
    private static final Logger logger = LoggerFactory.getLogger(VentaControlador.class.getName());

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> generar(@RequestBody VentaDto venta) {
        try {
            return ResponseEntity.status(201).body(ventaServicio.generar(venta));
        } catch (Exception e) {
            logger.error("Error al generar venta", e);
            return ResponseEntity.status(400).body(new HashMap<>(Map.of("error", e.getMessage())));
        }
    }


}
