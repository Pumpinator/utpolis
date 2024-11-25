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
public class DetalleVentaDto {

    private Long id;
    private Long ventaId;
    private Long productoId;
    private Long funcionId;
    private List<Long> asientosId;
    private int cantidad;
    private float subtotalDetalle;

}
