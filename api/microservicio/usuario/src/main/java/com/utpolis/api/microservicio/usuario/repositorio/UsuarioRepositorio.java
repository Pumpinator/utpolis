package com.utpolis.api.microservicio.usuario.repositorio;

import com.utpolis.modelo.entidad.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends CrudRepository<Usuario, Long>, PagingAndSortingRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByCorreo(String correo);

    Iterable<Usuario> findAllByRolNombre(String rol);

    Page<Usuario> findAllByRolNombre(String rol, Pageable pageable);

}
