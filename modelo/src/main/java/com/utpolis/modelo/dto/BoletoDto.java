package com.utpolis.modelo.dto;

import com.utpolis.modelo.entidad.Asiento;
import com.utpolis.modelo.entidad.Funcion;
import lombok.*;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BoletoDto {

    private Long id;
    private Asiento asiento_id;
    private Funcion funcion_id;
    private double precio;

}