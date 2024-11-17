package com.utpolis.api.microservicio.boleto.servicio;

import com.utpolis.api.microservicio.boleto.repositorio.BoletoRepositorio;
import com.utpolis.modelo.dto.BoletoDto;
import com.utpolis.modelo.entidad.Boleto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoletoServicio {
    private BoletoRepositorio boletoRepositorio;

    public BoletoServicio(BoletoRepositorio boletoRepositorio) {
        this.boletoRepositorio = boletoRepositorio;
    }

    public Iterable<BoletoDto> listar(){
        return ((List<Boleto>)boletoRepositorio.findAll()).stream().map(this::construirDto).toList();
    }

    public BoletoDto obtener(Long id){
        return construirDto(boletoRepositorio.findById(id).get());
    }


    public BoletoDto crear(@RequestBody BoletoDto boleto){
            return construirDto(boletoRepositorio.save(Boleto.builder()
                    .id(boleto.getId())
                    .asiento(boleto.getAsiento_id())
                    .funcion(boleto.getFuncion_id())
                    .precio(boleto.getPrecio())
                    .build()));
    }

    public BoletoDto construirDto(Boleto boleto){
        return BoletoDto.builder()
                .id(boleto.getId())
                .asiento_id(boleto.getAsiento())
                .funcion_id(boleto.getFuncion())
                .precio(boleto.getPrecio())
                .build();
    }
}