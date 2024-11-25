package com.utpolis.api.microservicio.venta.repositorio;

import com.utpolis.modelo.entidad.Funcion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionRepositorio extends CrudRepository<Funcion, Long> {
}
