package com.utpolis.api.microservicio.persona.repositorio;

import com.utpolis.modelo.entidad.Persona;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepositorio extends CrudRepository<Persona, Long> {

}
