package com.utpolis.api.microservicio.persona.repositorio;

import com.fasterxml.jackson.databind.introspect.AnnotationCollector;
import com.utpolis.modelo.entidad.Persona;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepositorio extends CrudRepository<Persona, Long>, PagingAndSortingRepository<Persona, Long> {

    Optional<Persona> findByTelefono(String telefono);
}
