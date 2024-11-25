package com.utpolis.api.microservicio.venta;

import com.utpolis.api.microservicio.venta.servicio.VentaServicio;


import com.utpolis.modelo.dto.DetalleVentaDto;
import com.utpolis.modelo.dto.VentaDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class VentaTest {

    @Autowired
    VentaServicio ventaServicio;

//    @Test
//    void generar() {
//        VentaDto venta = ventaServicio.generar(VentaDto.builder()
//                .empleadoId(2L)
//                .clienteId(3L)
//                .detalleVenta(List.of(
//                        DetalleVentaDto.builder()
//                                .funcionId(1L)
//                                .asientosId(List.of(37L, 38L))
//                                .cantidad(2)
//                                .build(),
//                        DetalleVentaDto.builder()
//                                .productoId(1L)
//                                .cantidad(1)
//                                .build()
//                ))
//                .build());
//        Assertions.assertThat(venta.getId()).isNotNull();
//    }

}
