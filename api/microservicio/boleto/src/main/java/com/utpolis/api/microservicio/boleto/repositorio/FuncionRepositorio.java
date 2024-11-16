package com.utpolis.api.microservicio.boleto.repositorio;

import com.utpolis.modelo.entidad.Boleto;
import com.utpolis.modelo.entidad.Funcion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionRepositorio extends CrudRepository<Funcion, Long> {
}
