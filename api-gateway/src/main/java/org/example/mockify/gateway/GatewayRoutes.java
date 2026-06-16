package org.example.mockify.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class GatewayRoutes {

    @Value("${services.account.url}")
    private String accountUrl;

    @Value("${services.transaction.url}")
    private String transactionUrl;

    @Value("${services.favorite.url}")
    private String favoriteUrl;

    @Value("${services.playlist.url}")
    private String playlistUrl;

    @Value("${services.subscription.url}")
    private String subscriptionUrl;

    @Bean
    public RouterFunction<ServerResponse> routes(ProxyHandler proxy) {
        return RouterFunctions.route()
                // Rotas específicas de /accounts/{id}/* antes da rota genérica /accounts/**
                .path("/api/v1/accounts/{accountId}/favorites",
                        b -> b.route(RequestPredicates.all(), r -> proxy.proxy(r, favoriteUrl)))
                .path("/api/v1/accounts/{accountId}/favorites/**",
                        b -> b.route(RequestPredicates.all(), r -> proxy.proxy(r, favoriteUrl)))
                .path("/api/v1/accounts/{accountId}/playlists",
                        b -> b.route(RequestPredicates.all(), r -> proxy.proxy(r, playlistUrl)))
                .path("/api/v1/accounts/{accountId}/playlists/**",
                        b -> b.route(RequestPredicates.all(), r -> proxy.proxy(r, playlistUrl)))
                .path("/api/v1/accounts/{accountId}/subscriptions",
                        b -> b.route(RequestPredicates.all(), r -> proxy.proxy(r, subscriptionUrl)))
                .path("/api/v1/accounts/{accountId}/subscriptions/**",
                        b -> b.route(RequestPredicates.all(), r -> proxy.proxy(r, subscriptionUrl)))
                // Rotas genéricas
                .path("/api/v1/accounts",
                        b -> b.route(RequestPredicates.all(), r -> proxy.proxy(r, accountUrl)))
                .path("/api/v1/accounts/**",
                        b -> b.route(RequestPredicates.all(), r -> proxy.proxy(r, accountUrl)))
                .path("/api/v1/transactions",
                        b -> b.route(RequestPredicates.all(), r -> proxy.proxy(r, transactionUrl)))
                .path("/api/v1/transactions/**",
                        b -> b.route(RequestPredicates.all(), r -> proxy.proxy(r, transactionUrl)))
                .path("/api/v1/songs",
                        b -> b.route(RequestPredicates.all(), r -> proxy.proxy(r, favoriteUrl)))
                .path("/api/v1/songs/**",
                        b -> b.route(RequestPredicates.all(), r -> proxy.proxy(r, favoriteUrl)))
                .build();
    }
}
