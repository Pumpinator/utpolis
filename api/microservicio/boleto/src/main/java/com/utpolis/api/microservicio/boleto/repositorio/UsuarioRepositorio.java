package com.utpolis.api.microservicio.boleto.repositorio;

import com.utpolis.modelo.entidad.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends CrudRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

}
