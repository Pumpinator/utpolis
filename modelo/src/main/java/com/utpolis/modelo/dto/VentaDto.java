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
    private Long usuarioId;
    private String fecha;
    private String estatus;
    private List<BoletoDto> boletos;

}
