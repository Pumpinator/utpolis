package com.utpolis.api.filtro;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JwtFiltro {

    Jwt<?, ?> jwt;

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

    public Collection<? extends GrantedAuthority> obtenerPermisos() {
        Collection<Map<String, String>> scopes = ((Claims) jwt.getBody()).get("scope", List.class);
        return scopes.stream().map(scope -> new SimpleGrantedAuthority(scope.get("authority"))).collect(Collectors.toList());
    }

    public String obtenerSujeto() {
        return ((Claims) jwt.getBody()).getSubject();
    }


}
