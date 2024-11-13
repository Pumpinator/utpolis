package com.utpolis.modelo.dto;

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
    private Long usuarioId;
    private Long horarioId;
    private String asiento;
    private double precio;
    private Instant fechaCompra;

}