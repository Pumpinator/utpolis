package mx.metaphorce.gateway.seguridad;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Base64;

@Component
public class AutorizacionFiltro extends AbstractGatewayFilterFactory<AutorizacionFiltro.Config> {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.prefix}")
    private String prefix;

    public AutorizacionFiltro() {
        super(Config.class);
    }

    public static class Config {}

    @Override
    public GatewayFilter apply(Config config) {
        return this::filtrar;
    }

    private Mono<Void> filtrar(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return denegar(exchange);
        }

        String authorization = request.getHeaders().get(header).get(0);
        String token = authorization.replace(prefix, "");

        if (!validar(token)) {
            return denegar(exchange);
        }

        return chain.filter(exchange);
    }

    private static Mono<Void> denegar(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
        exchange.getAttributes().put("error", "No autorizado");
        return response.setComplete();
    }

    private boolean validar(String token) {
        boolean tokenValido = true;
        String correo = null;

        try {
            correo = ((Jwt<Header, Claims>) Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(Base64.getEncoder().encode(secret.getBytes())))
                    .build().parse(token)).getBody().getSubject();
        } catch (Exception e) {
            tokenValido = false;
        }

        if (correo == null || correo.isEmpty()) {
            tokenValido = false;
        }

        return tokenValido;
    }
}