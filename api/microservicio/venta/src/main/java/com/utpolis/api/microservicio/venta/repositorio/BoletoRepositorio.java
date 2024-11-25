package com.utpolis.api.microservicio.venta.repositorio;

import com.utpolis.modelo.entidad.Boleto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoletoRepositorio extends CrudRepository<Boleto, Long> {
}
