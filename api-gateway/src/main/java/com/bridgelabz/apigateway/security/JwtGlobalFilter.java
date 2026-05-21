package com.bridgelabz.apigateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();


        if (path.startsWith("/api/auth") || path.startsWith("/oauth2") || path.startsWith("/login")) {
            return chain.filter(exchange);
        }

        String token = getToken(exchange);

        if (token == null || !jwtUtils.validate(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }


        String email = jwtUtils.getEmail(token);

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(exchange.getRequest()
                        .mutate()
                        .header("X-User-Email", email)
                        .build())
                .build();

        return chain.filter(mutatedExchange);
    }

    private String getToken(ServerWebExchange exchange) {
        String header = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7).trim();
        }
        return null;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}