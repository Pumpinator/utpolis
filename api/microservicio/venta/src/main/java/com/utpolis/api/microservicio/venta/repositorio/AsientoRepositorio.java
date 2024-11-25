package com.utpolis.api.microservicio.venta.repositorio;

import com.utpolis.modelo.entidad.Asiento;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsientoRepositorio extends CrudRepository<Asiento, Long> {
}
