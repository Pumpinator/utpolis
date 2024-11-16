package com.utpolis.api.microservicio.boleto.repositorio;

import com.utpolis.modelo.entidad.Boleto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoletoRepositorio extends CrudRepository<Boleto, Long> {
}
