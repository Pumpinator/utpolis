package com.utpolis.modelo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private double total;
    private List<DetalleVentaDto> detalleVenta;

}
