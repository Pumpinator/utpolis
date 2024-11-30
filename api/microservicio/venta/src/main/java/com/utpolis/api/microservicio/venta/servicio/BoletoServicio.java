package com.utpolis.api.microservicio.venta.servicio;

import com.utpolis.api.microservicio.venta.repositorio.AsientoRepositorio;
import com.utpolis.api.microservicio.venta.repositorio.BoletoRepositorio;
import com.utpolis.api.microservicio.venta.repositorio.FuncionRepositorio;
import com.utpolis.modelo.dto.BoletoDto;
import com.utpolis.modelo.entidad.Boleto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //Declaracion de Servicio
@RequiredArgsConstructor //Genera un constructor
public class BoletoServicio {

    //Repositorios atributos de clase que interactuan con la BD
    private final AsientoRepositorio asientoRepositorio;
    private final BoletoRepositorio boletoRepositorio;
    private final FuncionRepositorio funcionRepositorio;

    //Recupera los boletos de la BD y los comvierte en DTOs
    public Iterable<BoletoDto> listar() {
        //Convierte los boletos en una lista de Boleto, convierte cada Boleto en un BoletoDto y recolecta datos en una lista
        return ((List<Boleto>) boletoRepositorio.findAll()).stream().map(this::construirDto).toList();
    }

    //Obtiene un boleto por id, busca en la BD
    public BoletoDto obtener(Long id) {
        return construirDto(boletoRepositorio.findById(id).orElseThrow(() -> new RuntimeException(String.format("Boleto con id '%s' no encontrado", id))));
    }

    //Construye instancia de Boleto con los datos, valida el asiento y la funcion sean existentes
    public BoletoDto crear(BoletoDto boleto) {
        return construirDto(boletoRepositorio.save(Boleto.builder()
                .id(boleto.getId())
                .asiento(asientoRepositorio.findById(boleto.getAsientoId()).orElseThrow(() -> new RuntimeException(String.format("Asiento con id '%s' no encontrado", boleto.getAsientoId()))))
                .funcion(funcionRepositorio.findById(boleto.getFuncionId()).orElseThrow(() -> new RuntimeException(String.format("Funcion con id '%s' no encontrado", boleto.getFuncionId()))))
                .precio(boleto.getPrecio())
                .build()));
    }

    //Convierte un Boleto en un DTO (Data Transfere Object)
    public BoletoDto construirDto(Boleto boleto) {
        return BoletoDto.builder()
                .id(boleto.getId())
                .asientoId(boleto.getAsiento().getId())
                .funcionId(boleto.getFuncion().getId())
                .precio(boleto.getPrecio())
                .build();
    }
}
