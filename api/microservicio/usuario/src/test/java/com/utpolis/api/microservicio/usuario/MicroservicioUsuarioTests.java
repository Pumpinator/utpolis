package com.utpolis.api.microservicio.usuario;

import com.utpolis.api.microservicio.usuario.componente.DataComponente;
import com.utpolis.api.microservicio.usuario.servicio.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MicroservicioUsuarioTests {

    @Autowired
    ClienteServicio clienteServicio;

    @Autowired
    DataComponente dataComponente;

//    @Test
//    void obtenerClientePorId() {
//        Long id = 1L;
//        UsuarioDto cliente = clienteServicio.obtener(id);
//        System.out.println(cliente);
//    }
//
//    @Test
//    void verHora() {
//        System.out.println("Fecha: " + LocalDate.now());
//        System.out.println("Hora: " + LocalTime.now());
//    }
//
//    @Test
//    void crearDatos() {
//        dataComponente.crearDatos(null);
//    }

}
