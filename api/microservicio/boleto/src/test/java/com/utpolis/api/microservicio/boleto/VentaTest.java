package com.utpolis.api.microservicio.boleto;

import com.utpolis.api.microservicio.boleto.repositorio.*;
import com.utpolis.modelo.entidad.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.repository.CrudRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VentaTest {

    @Autowired
    VentaRepositorio ventaRepositorio;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    AsientoRepositorio asientoRepositorio;

    @Autowired
    FuncionRepositorio funcionRepositorio;

    @Autowired
    BoletoRepositorio boletoRepositorio;

    @Test
    void obtener() {
        Long id = 1L;
        Venta venta = ventaRepositorio.findById(id).orElseThrow(() -> new RuntimeException("No se encontró la venta con id " + id));
        System.out.println(venta);
    }

    @Test
    void fecha() {
        System.out.println(Instant.now());
    }

    @Test
    void vender() {
        Long usuarioId = 1L;
        Long asientoId = 1L;
        Long funcionId = 1L;
        double precio = 100.0;

        Asiento asiento = asientoRepositorio.findById(asientoId)
                .orElseThrow(() -> new RuntimeException("Asiento no encontrado"));
        if (asiento.isOcupado()) {
            throw new RuntimeException("Asiento ya ocupado");
        }
        Funcion funcion = funcionRepositorio.findById(funcionId)
                .orElseThrow(() -> new RuntimeException("Función no encontrada"));

        Boleto boleto = Boleto.builder()
                .funcion(funcion)
                .asiento(asiento)
                .precio(precio)
                .build();

        asiento.setOcupado(true);
        asientoRepositorio.save(asiento);
        boletoRepositorio.save(boleto);

    }

}
