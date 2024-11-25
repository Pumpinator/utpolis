package com.utpolis.api.microservicio.venta.servicio;

import com.utpolis.api.microservicio.venta.repositorio.AsientoRepositorio;
import com.utpolis.api.microservicio.venta.repositorio.BoletoRepositorio;
import com.utpolis.api.microservicio.venta.repositorio.FuncionRepositorio;
import com.utpolis.modelo.dto.BoletoDto;
import com.utpolis.modelo.entidad.Boleto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoletoServicio {

    private final AsientoRepositorio asientoRepositorio;
    private final BoletoRepositorio boletoRepositorio;
    private final FuncionRepositorio funcionRepositorio;

    public Iterable<BoletoDto> listar() {
        return ((List<Boleto>) boletoRepositorio.findAll()).stream().map(this::construirDto).toList();
    }

    public BoletoDto obtener(Long id) {
        return construirDto(boletoRepositorio.findById(id).orElseThrow(() -> new RuntimeException(String.format("Boleto con id '%s' no encontrado", id))));
    }

    public BoletoDto crear(BoletoDto boleto) {
        return construirDto(boletoRepositorio.save(Boleto.builder()
                .id(boleto.getId())
                .asiento(asientoRepositorio.findById(boleto.getAsientoId()).orElseThrow(() -> new RuntimeException(String.format("Asiento con id '%s' no encontrado", boleto.getAsientoId()))))
                .funcion(funcionRepositorio.findById(boleto.getFuncionId()).orElseThrow(() -> new RuntimeException(String.format("Funcion con id '%s' no encontrado", boleto.getFuncionId()))))
                .precio(boleto.getPrecio())
                .build()));
    }

    public BoletoDto construirDto(Boleto boleto) {
        return BoletoDto.builder()
                .id(boleto.getId())
                .asientoId(boleto.getAsiento().getId())
                .funcionId(boleto.getFuncion().getId())
                .precio(boleto.getPrecio())
                .build();
    }
}
