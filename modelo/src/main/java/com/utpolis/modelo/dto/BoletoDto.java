package com.utpolis.modelo.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BoletoDto {

    private Long id;
    private Long asientoId;
    private Long funcionId;
    private double precio;

}
