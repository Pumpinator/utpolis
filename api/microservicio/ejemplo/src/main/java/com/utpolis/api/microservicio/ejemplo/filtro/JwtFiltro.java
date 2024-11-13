package com.utpolis.api.microservicio.ejemplo.filtro;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;


public class JwtFiltro {

    private Jwt<?, ?> jwt;

    public JwtFiltro(String jwt, String secret) {
        this.jwt = parsear(jwt, secret);
    }

    private Jwt<?, ?> parsear(String jwt, String secret) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(Base64.getEncoder()
                        .encode(secret.getBytes()))
                )
                .build()
                .parse(jwt);
    }


    public String obtenerSujeto() {
        return ((Claims) jwt.getBody()).getSubject();
    }

}
