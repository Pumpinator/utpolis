package com.utpolis.modelo.dto;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VentaDto {

    private Long id;
    private Long empleadoId;
    private Long clienteId;
    private String fecha;
    private boolean estatus;
    private double total;
    private List<DetalleVentaDto> detalleVenta;

}
