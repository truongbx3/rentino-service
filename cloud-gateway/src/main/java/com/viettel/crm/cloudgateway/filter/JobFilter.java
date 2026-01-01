package com.viettel.crm.cloudgateway.filter;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JobFilter extends AbstractGatewayFilterFactory<JobFilter.Config> {

    @Value("${auth.job-verify-url}")
    private String jobVerifyUrl;
    private static final String AUTH_HEADER_NAME = "Authorization";

    private final WebClient.Builder webClientBuilder;

    public JobFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }
    private static final List<String> SWAGGER_WHITELIST = List.of(
            "/swagger-ui.html",
            "/swagger-ui",
            "/v3/api-docs",
            "/webjars"
    );
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();

            // Bypass authentication for Swagger URLs
            if (SWAGGER_WHITELIST.stream().anyMatch(path::contains)) {
                return chain.filter(exchange);
            }
            if (!exchange.getRequest().getHeaders().containsKey(AUTH_HEADER_NAME)) {
                return handleUnauthorized(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().get(AUTH_HEADER_NAME).get(0);

            return webClientBuilder.build()
                    .post()
                    .uri(jobVerifyUrl+"?token=" + authHeader)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .flatMap(isValid -> {
                        if (isValid) {
                            return chain.filter(exchange);
                        } else {
                            return handleUnauthorized(exchange);
                        }
                    })
                    .onErrorResume(e -> handleUnauthorized(exchange));
        };
    }
    private Mono<Void> handleUnauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete(); // Ensure response is completed
    }
    public static class Config {
        // empty class as I don't need any particular configuration
    }
}
