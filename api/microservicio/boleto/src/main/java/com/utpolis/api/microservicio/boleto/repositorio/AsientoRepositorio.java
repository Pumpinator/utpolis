package com.utpolis.api.microservicio.boleto.repositorio;

import com.utpolis.modelo.entidad.Asiento;
import org.springframework.data.repository.CrudRepository;

public interface AsientoRepositorio extends CrudRepository<Asiento, Long> {
}
