package com.utpolis.api.microservicio.usuario;

import com.utpolis.api.microservicio.usuario.servicio.ClienteServicio;
import com.utpolis.modelo.dto.UsuarioDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MicroservicioUsuarioTests {

    @Autowired
    ClienteServicio clienteServicio;

    @Test
    void obtenerClientePorId() {
        Long id = 1L;
        UsuarioDto cliente = clienteServicio.obtener(id);
        System.out.println(cliente);
    }

}
