package com.utpolis.api.microservicio.usuario.filtro;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.utpolis.api.microservicio.usuario.servicio.UsuarioDetallesImpl;
import com.utpolis.modelo.entidad.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.*;


public class AutenticacionFiltro extends UsernamePasswordAuthenticationFilter { // Filtro de autenticación

    private final String secret; // Información sensible que se encuentra en el archivo application.properties
    private final long expiration; // Información sensible que se encuentra en el archivo application.properties

    public AutenticacionFiltro(AuthenticationManager authenticationManager, String secret, long expiration) { // Constructor con herencia de UsernamePasswordAuthenticationFilter
        super(authenticationManager);
        this.secret = secret;
        this.expiration = expiration;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException { // Método para intentar autenticar
        try {
            Usuario usuario = new ObjectMapper().readValue(req.getInputStream(), Usuario.class); // Leer datos de la solicitud y convertir a objeto Usuario
            UsuarioDetallesImpl userDetails = new UsuarioDetallesImpl(usuario); // Crear detalles del usuario
            return getAuthenticationManager()
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userDetails.getUsername(),
                                    userDetails.getPassword(),
                                    new ArrayList<>()
                            )
                    ); // Autenticar usuario
        } catch (Exception e) { // Excepción en caso de error
            Map<String, String> data = new HashMap<>(Map.of("error", e.getMessage())); // Datos en un mapa
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Código de estado
            res.setContentType(MediaType.APPLICATION_JSON_VALUE); // Tipo de contenido
            res.getOutputStream().println(new ObjectMapper().writeValueAsString(data)); // Convertir a JSON el mapa
            return null;
        }
    }

    @Override // Método para autenticación exitosa
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
        UsuarioDetallesImpl userDetails = (UsuarioDetallesImpl) auth.getPrincipal(); // Obtener detalles del usuario

        String token = Jwts.builder()
                .claim("scope", auth.getAuthorities())
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(expiration)))
                .signWith(Keys.hmacShaKeyFor(Base64.getEncoder().encode(secret.getBytes())))
                .compact(); // Crear token

        Map<String, String> datos = new HashMap<>(Map.of(
                "id", String.valueOf(userDetails.getUsuario().getId()),
                "username", userDetails.getUsername(),
                "rol", obtenerRol(auth.getAuthorities()),
                "expira", (expiration / 60) + " minutos",
                "token", token
        )); // Datos del token en un mapa

        res.setStatus(HttpServletResponse.SC_OK); // Código de estado
        res.setContentType(MediaType.APPLICATION_JSON_VALUE); // Tipo de contenido
        res.getOutputStream().println(new ObjectMapper().writeValueAsString(datos)); // Convertir a JSON los datos
    }

    private String obtenerRol(Collection<? extends GrantedAuthority> authorities) {
        String rol = authorities.stream().findFirst().orElseThrow(
                () -> new IllegalArgumentException("No se encontró el rol del usuario")
        ).getAuthority().replace("ROLE_", "").replace("[", "").replace("]", "");
        return rol.charAt(0) + rol.substring(1).toLowerCase();
    }

    @Override // Método para autenticación fallida
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
        Map<String, String> data = new HashMap<>(Map.of("error", "Credenciales incorrectas")); // Datos en un mapa

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.getOutputStream().println(new ObjectMapper().writeValueAsString(data));
    }
}
