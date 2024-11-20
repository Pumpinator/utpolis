package com.utpolis.modelo.dto;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DetalleVentaDto {

    private Long ventaId;
    private Long productoId;
    private Long funcionId;
    private List<Long> asientosId;
    private int cantidad;
    private double subtotalDetalle;

}
